package com.github.ezequielarroyo.postservice.entities;

import com.github.ezequielarroyo.postservice.dtos.input.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class UserSnapshot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;
    @Column(nullable = false, unique = true)
    private String username;
    private String avatar;

    private UserSnapshot(UUID uuid, String username, String avatar) {
        this.uuid = uuid;
        this.username = username;
        this.avatar = avatar;
    }
    public static UserSnapshot create(UUID uuid, String username, String avatar) {
        return new UserSnapshot(uuid, username, avatar);
    }
    public void update(UserDTO userDTO) {
        this.changeUsername(userDTO.username());
        this.changeAvatar(userDTO.avatar());
    }
    private void changeUsername(String username) {
        if(username != null && !username.isBlank()) {
            this.username = username;
        }
    }
    private void changeAvatar(String avatar) {
        if(avatar != null && !avatar.isBlank()) {
            this.avatar = avatar;
        }
    }

}