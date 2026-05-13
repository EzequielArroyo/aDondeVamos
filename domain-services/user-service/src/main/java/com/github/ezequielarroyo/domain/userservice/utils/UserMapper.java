package com.github.ezequielarroyo.domain.userservice.utils;

import com.github.ezequielarroyo.domain.userservice.dtos.UserCreateRequest;
import com.github.ezequielarroyo.domain.userservice.dtos.UserResponse;
import com.github.ezequielarroyo.domain.userservice.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .uuid(user.getUuid())
                .username(user.getUsername())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .isCompleted(user.getIsCompleted())
                .build();
    }

    public User toUser(UserCreateRequest userCreateRequest) {
        return User.create(
                userCreateRequest.username(),
                userCreateRequest.name(),
                userCreateRequest.lastName(),
                userCreateRequest.email()
        );
    }
}
