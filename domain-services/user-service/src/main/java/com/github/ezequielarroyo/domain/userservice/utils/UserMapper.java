package com.github.ezequielarroyo.domain.userservice.utils;

import com.github.ezequielarroyo.domain.userservice.dtos.*;
import com.github.ezequielarroyo.domain.userservice.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .uuid(user.getUuid())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastName(user.getLastname())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .build();
    }

    public User toUser(UserCreateRequest request) {
        return User.create(
                request.username(),
                request.firstname(),
                request.lastname(),
                request.email(),
                request.avatar()
        );
    }
    public UserProfileData toUserProfileData(UserUpdateRequest user) {
        return UserProfileData.builder()
                .username(user.username())
                .firstname(user.firstname())
                .lastname(user.lastname())
                .email(null)
                .avatar(user.avatar())
                .build();
    }

}
