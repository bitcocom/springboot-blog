package com.cos.blog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController {

    // http://localhost:9000/test/hello
    @GetMapping("/test/hello")
    public String hello(){
        return "<h1>hello test</h1>";
    }
}
