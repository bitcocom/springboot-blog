package com.cos.blog.test;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Supplier;

@RestController
public class DummyControllerTest {

    @Autowired // 의존성 주입(DI)
    private UserRepository userRepository;
    @PostMapping("/dummy/join")
    public String join(User user){ // form을 이용하여 데이터를 받음
        System.out.println(user);
        //user.setRole("user2"); 실수를 할 수 있다.
        //넣을 수 있는 범위를 만들어 준다.(Enum)
        user.setRole(RoleType.USER); // 실수 방지
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }
    @GetMapping("/dummy/user/{id}")
    public User detail(@PathVariable Long id){
        //user/4를 찾으면 내가 데이터베이스에서 못찾아오게 되면 user가 null이 될것 아냐?
        //그럼 return null이 리턴이 되자나. 그럼 프로그램에 문제가 있지 않겠니?
        //Optional로 너의 user객체를 감싸서 가져올테니 null인지 아닌지 판단해서 return해!!!
        // 람다식 : ()->{    }
        User user=userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 없습니다. id:"+id); //AOP 에러 페이지로...
            }
        });
        // 요청 : 웹브라우저
        // user객체 = 자바 오브젝트
        // 변환(웹브라우저가 이해할 수 있는 데이터) -> json(Gson 라이브러리)
        // 스프링부트 = MessageConverter라는 애가 응답시에 자동 작동
        // 만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson라이브러리를 호출해서
        // user오브젝트를 json으로 변환해서 브라우져에게 던져줍니다.
        return user;
    }
    @GetMapping("/dummy/users")
    public List<User> list(){
      return userRepository.findAll();
    }
    // 한페이지당 2건에 데이터를 리턴받아볼 예정
    @GetMapping("/dummy/user")
    public List<User> pageList(@PageableDefault(size=2, sort="id", direction= Sort.Direction.DESC) Pageable pageable){
        Page<User> pageUsers=userRepository.findAll(pageable);
        // pageUsers : 추가적인 처리에 사용
        List<User> users=pageUsers.getContent();
        return users;
    }
    // email, password
    @Transactional // 함수 종료시 자동 commit이 된다. ->User의 값이 변경이 되어있으면 update시킴
    @PutMapping("/dummy/user/{id}")               //json데이터를 -> Object로 받기
    public User updateUser(@PathVariable Long id, @RequestBody User requestUser){
        System.out.println(id);
        System.out.println(requestUser);

        User user =userRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("수정에 실패하였습니다.");
        });
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());
        // save함수는 id를 전달하지 않으면 insert를 해주고
        // save함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update를 해주고
        // save함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert를 해요!!
        // userRepository.save(user);
        // 더티 체킹
        return user;
    }

    @DeleteMapping("/dummy/user/{id}")
    public String delete(@PathVariable Long id){
        try {
            userRepository.deleteById(id);
        }catch (Exception e){
           return "삭제에 실패하였습니다.해당 id는 DB에 없습니다.";
        }
        return "삭제되었습니다.id +" +id;
    }
}
