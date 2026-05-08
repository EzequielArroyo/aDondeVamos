package com.github.ezequielarroyo.domain.userservice.services;

import com.github.ezequielarroyo.domain.userservice.dtos.UserCreateRequest;
import com.github.ezequielarroyo.domain.userservice.dtos.UserUpdateRequest;
import com.github.ezequielarroyo.domain.userservice.dtos.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IUserService {
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUserByUuid(UUID id);
    UserResponse getUserByUsername(String username);
    UUID createUser(UserCreateRequest request);
    UserResponse getCurrentUser();
    void updateUser(UserUpdateRequest request);
    void deleteUser(UUID id);
}
