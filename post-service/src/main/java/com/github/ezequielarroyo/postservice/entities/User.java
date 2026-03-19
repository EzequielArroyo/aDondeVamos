package com.github.ezequielarroyo.postservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;
    @Column(nullable = false, unique = true)
    private String username;
    private String avatar;

    private User(String username, String avatar) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.avatar = avatar;
    }
    public static User create(String username, String avatar) {
        return new User(username, avatar);
    }

}