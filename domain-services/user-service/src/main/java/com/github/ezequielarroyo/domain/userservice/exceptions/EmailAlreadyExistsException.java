package com.github.ezequielarroyo.domain.userservice.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String username) {
        super("Email already used: " + username);
    }
}
