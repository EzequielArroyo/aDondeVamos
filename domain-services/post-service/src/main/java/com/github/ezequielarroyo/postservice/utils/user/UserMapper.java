package com.github.ezequielarroyo.postservice.utils.user;

import com.github.ezequielarroyo.postservice.dtos.input.UserDTO;
import com.github.ezequielarroyo.postservice.entities.UserSnapshot;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserSnapshot toEntity(UserDTO dto) {
        return UserSnapshot.create(dto.userId(), dto.username(), dto.avatar());
    }
    public UserDTO toDTO(UserSnapshot user) {
        return UserDTO.builder()
                .userId(user.getUuid())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .build();
    }
}
