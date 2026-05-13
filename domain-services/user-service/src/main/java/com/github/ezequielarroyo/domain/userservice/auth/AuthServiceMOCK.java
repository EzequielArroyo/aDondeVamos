package com.github.ezequielarroyo.domain.userservice.auth;

import com.github.ezequielarroyo.domain.userservice.entities.User;
import com.github.ezequielarroyo.domain.userservice.repositories.IUserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthServiceMOCK implements IAuthService{
    private final IUserRepository userRepository;

    public AuthServiceMOCK(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UUID getCurrentUserId() {
        User user = userRepository.findAll().getFirst();
        return user.getUuid();
    }
}
