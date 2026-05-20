package com.github.ezequielarroyo.domain.userservice.entities;

import com.github.ezequielarroyo.domain.userservice.dtos.UserProfileData;
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

    @Column(unique = true)
    private String username;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column(unique = true)
    private String email;

    private String avatar;

    private User(String username, String firstname, String lastname, String email, String avatar) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.avatar = avatar;
    }
    public static User create(String username, String name, String lastname, String email, String avatar) {
        return new User(username, name, lastname, email, avatar);
    }
    public void update(UserProfileData request) {
        this.changeUsername(request.username());
        this.changeName(request.firstname());
        this.changeLastName(request.lastname());
        this.changeAvatar(request.avatar());
    }

    public void changeUsername(String username) {
        if(username != null && !username.isBlank()) {
            this.username = username;
        }
    }

    public void changeName(String name) {
        if(name != null && !name.isBlank()) {
            this.firstname = name;
        }
    }

    public void changeLastName(String lastname) {
        if(lastname != null && !lastname.isBlank()) {
            this.lastname = lastname;
        }
    }

    public void changeAvatar(String avatar) {
        if(avatar != null && !avatar.isBlank()) {
            this.avatar = avatar;
        }
    }
}
