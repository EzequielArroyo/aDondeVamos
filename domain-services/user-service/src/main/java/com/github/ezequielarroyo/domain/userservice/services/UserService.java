package com.github.ezequielarroyo.domain.userservice.services;
import com.github.ezequielarroyo.domain.userservice.auth.IAuthService;
import com.github.ezequielarroyo.domain.userservice.dtos.*;
import com.github.ezequielarroyo.domain.userservice.entities.User;
import com.github.ezequielarroyo.domain.userservice.exceptions.EmailAlreadyExistsException;
import com.github.ezequielarroyo.domain.userservice.exceptions.UserAlreadyExistsException;
import com.github.ezequielarroyo.domain.userservice.exceptions.UserNotFoundException;
import com.github.ezequielarroyo.domain.userservice.repositories.IUserRepository;
import com.github.ezequielarroyo.domain.userservice.utils.UserMapper;
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
        User userFound = this.findUserByUsername(username);
        return mapper.toUserResponse(userFound);
    }
    //TODO: add a petition to PostService to create a snapshot of the recent user created
    @Override
    public UUID createUser(UserCreateRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException(request.email());
        }
        User userToCreate = mapper.toUser(request);
        User createdUser = userRepository.save(userToCreate);
        return createdUser.getUuid();
    }
    @Override
    public UserResponse getCurrentUser() {
        User userFound = this.findCurrentUser();
        return mapper.toUserResponse(userFound);
    }
    //TODO: add a petition to PostService to update the snapshot of the recent user updated
    @Override
    public void updateUser(UserUpdateRequest request) {
        User userToUpdate = this.findCurrentUser();
        this.validateUsername(request.username(), userToUpdate);
        UserProfileData profileData = mapper.toUserProfileData(request);
        userToUpdate.update(profileData);
        userRepository.save(userToUpdate);
    }
    //TODO: chance to logical delete
    @Override
    public void deleteUser(UUID id) {
        User userToDelete = this.findUserByUuid(id);
        userRepository.delete(userToDelete);
    }

    private User findUserByUuid(UUID uuid){
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException(uuid.toString()));
    }
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
    private User findCurrentUser(){
        UUID userUuid = authService.getCurrentUserId();
        return this.findUserByUuid(userUuid);
    }

    private void validateUsername(String username, User existingUser) {
        if (username == null) {
            return;
        }

        boolean usernameAlreadyExists =
                userRepository.existsByUsername(username);

        boolean usernameChanged = existingUser.getUsername() == null || !existingUser.getUsername().equals(username);

        if (usernameChanged && usernameAlreadyExists) {
            throw new UserAlreadyExistsException(username);
        }
    }
}
