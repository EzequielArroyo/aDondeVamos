package com.github.ezequielarroyo.domain.userservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(unique = true, nullable = false, updatable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String avatar;

    @Column
    private Boolean isCompleted;

    private User(String username, String name,String lastName, String email) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.avatar = null;
        this.isCompleted = false;
    }
    public static User create(String username, String name, String lastName, String email){
        return new User(username, name, lastName, email);
    }

    public void changeUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        this.username = username;
    }

    public void changeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }

    public void changeLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("LastName cannot be null or blank");
        }
        this.lastName = lastName;
    }

    public void changeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        this.email = email;
    }

    public void changeAvatar(String avatar) {
        if (avatar == null || avatar.isBlank()) {
            throw new IllegalArgumentException("Avatar cannot be null or blank");
        }
        this.avatar = avatar;
    }

    public void markAsComplete(Boolean isCompleted) {
        if (isCompleted == null) {
            throw new IllegalArgumentException("IsCompleted cannot be null");
        }
        this.isCompleted = isCompleted;
    }
}
