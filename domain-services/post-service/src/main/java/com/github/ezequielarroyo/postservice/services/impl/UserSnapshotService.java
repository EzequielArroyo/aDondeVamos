package com.github.ezequielarroyo.postservice.services.impl;

import com.github.ezequielarroyo.postservice.dtos.input.UserDTO;
import com.github.ezequielarroyo.postservice.entities.UserSnapshot;
import com.github.ezequielarroyo.postservice.exceptions.UserSnapshotNotFoundException;
import com.github.ezequielarroyo.postservice.repositories.IUserSnapshotRepository;
import com.github.ezequielarroyo.postservice.services.IUserSnapshotService;
import com.github.ezequielarroyo.postservice.utils.user.UserMapper;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserSnapshotService implements IUserSnapshotService {
    private final IUserSnapshotRepository userSnapshotRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUserSnapshot(UserDTO user) {
        validateInput(user);
        UserSnapshot snapshot = userMapper.toEntity(user);
        UserSnapshot savedSnapshot = userSnapshotRepository.save(snapshot);
        return userMapper.toDTO(savedSnapshot);
    }

    @Override
    public UserDTO updateUserSnapshot(UserDTO user) {
        validateInput(user);
        UserSnapshot existingSnapshot = this.findByUuid(user.userId());
        existingSnapshot.update(user);
        UserSnapshot updatedSnapshot = userSnapshotRepository.save(existingSnapshot);
        return userMapper.toDTO(updatedSnapshot);
    }

    @Override
    public void deleteUserSnapshot(UUID userId) {
        if(!userSnapshotRepository.existsByUuid(userId)) {
            throw new UserSnapshotNotFoundException("UserSnapshot not found for userId: " + userId);
        }
        userSnapshotRepository.deleteByUuid(userId);
    }

    private UserSnapshot findByUuid(UUID userId) {
        return userSnapshotRepository.findByUuid(userId)
                .orElseThrow(() -> new UserSnapshotNotFoundException("UserSnapshot not found for userId: " + userId));
    }

    private void validateInput(UserDTO user) {
        if(user == null) {
            throw new IllegalArgumentException("UserDTO is null");
        }
        if(user.userId() == null) {
            throw new IllegalArgumentException("UserDTO userId is null");
        }
         if(user.username() == null || user.username().isBlank()) {
            throw new IllegalArgumentException("UserDTO username is null or blank");
        }
        if(user.avatar() == null || user.avatar().isBlank()) {
            throw new IllegalArgumentException("UserDTO avatar is null or blank");
        }
    }
}

