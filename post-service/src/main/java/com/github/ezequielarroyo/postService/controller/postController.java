package com.github.ezequielarroyo.postService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class postController {
    @GetMapping()
    public String helloWorld(){
        return "hello world";
    }
}
