package com.sx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/employee/basic/hello")
    public String test(){
        return "hello";
    }
    @GetMapping("/employee/advanced/hello")
    public String test2(){
        return "hello2";
    }
}
