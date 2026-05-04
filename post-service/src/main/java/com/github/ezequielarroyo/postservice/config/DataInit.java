package com.github.ezequielarroyo.postservice.config;

import com.github.ezequielarroyo.postservice.entities.User;
import com.github.ezequielarroyo.postservice.repositories.IUserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataInit implements CommandLineRunner {
    private final IUserRepository userRepository;

    public DataInit(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String @NonNull ... args){
        UUID userId = UUID.randomUUID();
        User user = User.create(userId, "testuser", "https://example.com/avatar.png");
        userRepository.save(user);
        System.out.println("Sample user created with ID: " + userId);
        System.out.println("Fecha: " + LocalDateTime.now());
        System.out.println("Data initialization logic goes here.");
    }
}
