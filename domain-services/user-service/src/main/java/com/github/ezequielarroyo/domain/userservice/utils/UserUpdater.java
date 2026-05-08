package com.github.ezequielarroyo.domain.userservice.utils;

import com.github.ezequielarroyo.domain.userservice.dtos.UserRequest;
import com.github.ezequielarroyo.domain.userservice.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserUpdater {
    public User updateUser(User user, UserRequest request) {
        user.changeUsername(request.username());
        user.changeName(request.name());
        user.changeLastName(request.lastname());
        user.changeEmail(request.email());
        user.changeAvatar(request.avatar());
        return user;
    }
}
