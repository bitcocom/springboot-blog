package com.cos.blog.test;

import org.springframework.web.bind.annotation.*;

@RestController
public class HttpControllerTest {
    // GET:요청?사과 / GET:요청?1
    @GetMapping("/http/get") // (어떤 데이터를)데이터를 줘!(select)
    public String getTest(Member m){ // id=1&username=aaa&password=12345&email=bit@empas.com
        // Member.builder().
        System.out.println(m.getId()+","+m.getUsername());
        return "get 요청 "+m.getId()+","+m.getUsername();
    }
    // POST:요청 : id=user,pass=1234, .....
    @PostMapping("/http/post")// (어떤 데이터를)데이터를 추가해 줘!(insert)
    //                                   text/plain, application/json
    public String postTest(@RequestBody Member m){ // MessageConverter(SpringBoot)
        return "post 요청 : " + m.getId()+","+m.getUsername(); // json
    }
    @PutMapping("/http/put")// (어떤 데이터를)데이터를 수정해 줘!(update)
    public String putTest(){
        return "put 요청";
    }
    @DeleteMapping("/http/delete")// (어떤 데이터를)데이터를 삭제해 줘!(delete)
    public String deleteTest(){
        return "delete 요청";
    }
}
