package com.github.ezequielarroyo.postservice.exceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String post) {
        super("Post not found: " + post);
    }
}
