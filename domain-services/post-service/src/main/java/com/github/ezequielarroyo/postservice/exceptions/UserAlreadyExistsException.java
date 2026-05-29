package com.github.ezequielarroyo.postservice.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String userId) {
        super("user already exists: " + userId);
    }
}
