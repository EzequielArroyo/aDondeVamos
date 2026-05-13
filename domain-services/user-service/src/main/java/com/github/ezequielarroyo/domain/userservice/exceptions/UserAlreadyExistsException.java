package com.github.ezequielarroyo.domain.userservice.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("username already used: " + username);
    }
}
