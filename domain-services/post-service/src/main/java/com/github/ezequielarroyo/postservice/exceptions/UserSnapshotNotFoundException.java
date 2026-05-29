package com.github.ezequielarroyo.postservice.exceptions;

public class UserSnapshotNotFoundException extends RuntimeException {
    public UserSnapshotNotFoundException(String userId) {
        super("user not found: " + userId);
    }
}
