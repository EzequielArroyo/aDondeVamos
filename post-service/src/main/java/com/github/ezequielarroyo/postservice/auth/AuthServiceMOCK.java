package com.github.ezequielarroyo.postservice.auth;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthServiceMOCK implements IAuthService{
    @Override
    public UUID getCurrentUserId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }
}
