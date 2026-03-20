package com.github.ezequielarroyo.postservice.services.impl;

import com.github.ezequielarroyo.postservice.entities.User;
import com.github.ezequielarroyo.postservice.repositories.IUserRepository;
import com.github.ezequielarroyo.postservice.services.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntityNotFoundException("User not found with uuid " + uuid));
    }
}
