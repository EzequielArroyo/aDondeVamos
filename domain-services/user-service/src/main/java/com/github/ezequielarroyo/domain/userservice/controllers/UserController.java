package com.github.ezequielarroyo.domain.userservice.controllers;

import com.github.ezequielarroyo.domain.userservice.dtos.UserCreateRequest;
import com.github.ezequielarroyo.domain.userservice.dtos.UserUpdateRequest;
import com.github.ezequielarroyo.domain.userservice.dtos.UserResponse;
import com.github.ezequielarroyo.domain.userservice.services.IUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@Slf4j
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        Page<UserResponse> userResponsePage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(userResponsePage);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        UserResponse userResponse = userService.getUserByUuid(id);
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse userResponse = userService.getUserByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UUID> createUser(@Valid @RequestBody UserCreateRequest request) {
        UUID userId = userService.createUser(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userId)
                .toUri();
        return ResponseEntity.created(location).body(userId);
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        log.debug("REST request to get profile");
        UserResponse response = userService.getCurrentUser();
        log.debug("END of REST request to get profile");
        return ResponseEntity.ok(response);
    }
    @PutMapping("/me")
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        userService.updateUser(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
       return ResponseEntity.noContent().build();
    }
}
