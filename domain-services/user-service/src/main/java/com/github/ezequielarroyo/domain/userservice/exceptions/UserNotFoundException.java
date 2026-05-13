package com.github.ezequielarroyo.domain.userservice.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String user) {
        super("User not found: " + user);
    }
}
