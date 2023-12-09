package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {
    // http://localhost:9000/blog/temp/home
   @GetMapping("/temp/home")
   public String home(){
       return "/home.html";
   }
    @GetMapping("/temp/jsp")
    public String tempJsp(){
        return "test";
    }
}
