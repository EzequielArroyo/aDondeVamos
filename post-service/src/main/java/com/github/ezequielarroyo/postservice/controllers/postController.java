package com.github.ezequielarroyo.postservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class postController {
    @GetMapping(value = "/ping")
    public String helloWorld(){
        return "Adonde vamos respond: pong";
    }
}
