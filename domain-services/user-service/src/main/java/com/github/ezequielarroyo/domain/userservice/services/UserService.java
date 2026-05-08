package com.github.ezequielarroyo.domain.userservice.services;
import com.github.ezequielarroyo.domain.userservice.auth.IAuthService;
import com.github.ezequielarroyo.domain.userservice.dtos.UserRequest;
import com.github.ezequielarroyo.domain.userservice.dtos.UserResponse;
import com.github.ezequielarroyo.domain.userservice.entities.User;
import com.github.ezequielarroyo.domain.userservice.repositories.IUserRepository;
import com.github.ezequielarroyo.domain.userservice.utils.UserMapper;
import com.github.ezequielarroyo.domain.userservice.utils.UserUpdater;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final IUserRepository userRepository;
    private final IAuthService authService;
    private final UserMapper mapper;
    private final UserUpdater userUpdater;

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> response = userRepository.findAll(pageable);
        return response.map(mapper::toUserResponse);
    }

    @Override
    public UserResponse getUserByUuid(UUID uuid) {
        User userFound = this.findUserByUuid(uuid);
        return mapper.toUserResponse(userFound);
    }
    @Override
    public UserResponse getUserByUsername(String username) {
        User userFound = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with uuid: " + username));
        return mapper.toUserResponse(userFound);
    }

    @Override
    public UUID createUser(UserRequest request) {
        User userToCreate = mapper.toUser(request);
        User createdUser = userRepository.save(userToCreate);
        return createdUser.getUuid();
    }
    @Override
    public UserResponse getCurrentUser() {
        UUID userUuid = authService.getCurrentUserId();
        User userFound = this.findUserByUuid(userUuid);
        return mapper.toUserResponse(userFound);
    }

    @Override
    public void updateUser(UserRequest request) {
        UUID userUuid = authService.getCurrentUserId();
        User existingUser = this.findUserByUuid(userUuid);
        User userToUpdate = userUpdater.updateUser(existingUser, request);
        userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUser(UUID id) {
        User userToDelete = this.findUserByUuid(id);
        userRepository.delete(userToDelete);
    }
    private User findUserByUuid(UUID uuid){
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("User not found with uuid: " + uuid));
    }
}
