package com.github.ezequielarroyo.postservice.auth;

import com.github.ezequielarroyo.postservice.entities.UserSnapshot;
import com.github.ezequielarroyo.postservice.repositories.IUserSnapshotRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthServiceMOCK implements IAuthService{
    private final IUserSnapshotRepository userRepository;

    public AuthServiceMOCK(IUserSnapshotRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UUID getCurrentUserId() {
        UserSnapshot user = userRepository.findAll().getFirst();
        return user.getUuid();
    }
}
