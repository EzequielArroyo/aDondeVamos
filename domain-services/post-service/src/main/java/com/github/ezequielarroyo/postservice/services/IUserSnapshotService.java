package com.github.ezequielarroyo.postservice.services;

import com.github.ezequielarroyo.postservice.dtos.input.UserDTO;

import java.util.UUID;

public interface IUserSnapshotService {
    UserDTO createUserSnapshot(UserDTO user);
    UserDTO updateUserSnapshot(UserDTO user);
    void deleteUserSnapshot(UUID userId);
}
