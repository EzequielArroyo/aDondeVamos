package com.github.ezequielarroyo.postmodule.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class postController {
    @GetMapping()
    public String helloWorld(){
        return "hello world";
    }
}
