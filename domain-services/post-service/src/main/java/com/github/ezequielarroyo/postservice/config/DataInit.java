package com.github.ezequielarroyo.postservice.config;

import com.github.ezequielarroyo.postservice.entities.UserSnapshot;
import com.github.ezequielarroyo.postservice.repositories.IUserSnapshotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataInit implements CommandLineRunner {
    private final IUserSnapshotRepository userRepository;

    public DataInit(IUserSnapshotRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String ...args){
        if(userRepository.count()==0){
            UUID userId = UUID.randomUUID();
            UserSnapshot user = UserSnapshot.create(userId, "testuser", "https://example.com/avatar.png");
            userRepository.save(user);
            System.out.println("Sample user created with ID: " + userId);
            System.out.println("Fecha: " + LocalDateTime.now());
            System.out.println("Data initialization logic goes here.");
        }

    }
}
