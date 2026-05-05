package com.github.ezequielarroyo.postservice.utils.user;

import com.github.ezequielarroyo.postservice.dtos.input.UserDTO;
import com.github.ezequielarroyo.postservice.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    User toEntity(UserDTO dto) {
        return User.create(dto.userId(), dto.username(), dto.avatar());
    }
}
