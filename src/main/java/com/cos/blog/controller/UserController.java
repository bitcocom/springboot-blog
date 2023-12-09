package com.cos.blog.controller;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용
// 그냥 주소가 / 이면  index.jsp 허용
// static이하에 있는 /js/**, /css/**, /image/**
@Controller
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;
    @GetMapping("/auth/joinForm")
    public String joinForm(){
        return "user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm(){
        return "user/loginForm";
    }
    @GetMapping("/user/updateForm")
    public String updateForm(){
        return "user/updateForm";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code){

        // POST방식으로 key=value 데이터를 요청(카카오톡 쪽으로)
        // Retrofit2
        // OkHttp
        // RestTemplate
        RestTemplate rt=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params=new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","ddcaeaee21f163c58818ca2db1820c67");
        params.add("redirect_uri","http://localhost:9000/auth/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest=
                new HttpEntity<>(params, headers);

        // Http요청하기
        ResponseEntity<String> response=rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        // Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper=new ObjectMapper();
        OAuthToken oAuthToken=null;
        try {
           oAuthToken=objectMapper.readValue(response.getBody(),OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("카카오 엑세스 토큰"+ oAuthToken.getAccess_token());
        // 사용자 인증정보 기져오기
        RestTemplate rt2=new RestTemplate();

        HttpHeaders headers2=new HttpHeaders();
        headers2.add("Authorization","Bearer "+oAuthToken.getAccess_token());
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2=
                new HttpEntity<>(headers2);

        // Http요청하기
        ResponseEntity<String> response2=rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

        System.out.println(response2.getBody());

        ObjectMapper objectMapper2=new ObjectMapper();
        KakaoProfile kakaoProfile=null;
        try {
            kakaoProfile=objectMapper2.readValue(response2.getBody(),KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // User 오브젝트 : username, password, email
        System.out.println("카카오 아이디(번호):"+ kakaoProfile.getId());
        System.out.println("카카오 이메일:"+ kakaoProfile.getKakao_account().getEmail());

        System.out.println("블로그서버 유저네임:"+ kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
        System.out.println("블로그서버 이메일:"+ kakaoProfile.getKakao_account().getEmail());
        //UUID garbagePassword=UUID.randomUUID();
        System.out.println("블로그서버 패스워드:"+ "임의의 패스워드");
        // 강제로 회원 가입
        User kakaoUser= User.builder()
                        .username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
                        .password("임의의 패스워드")
                        .email(kakaoProfile.getKakao_account().getEmail())
                        .oauth("kakao")
                        .build();

        // 가입자 혹은 비가입자 체크 해서 처리
        User originUser=userService.search(kakaoUser.getUsername());
        if(originUser.getUsername() == null){
            System.out.println("기존 회원이 아니기에 자동 회원가입 처리............");
            userService.register(kakaoUser);
        }
        System.out.println("자동 로그인을 진행 합니다.");
        // 로그인 처리
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), "임의의 패스워드"));
        // Update the SecurityContext with the new authentication
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return "redirect:/" ;
        //https://www.jsonschema2pojo.org/
    }
}
