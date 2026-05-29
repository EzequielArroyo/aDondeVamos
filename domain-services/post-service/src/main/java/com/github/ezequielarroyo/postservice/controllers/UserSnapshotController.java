package com.github.ezequielarroyo.postservice.controllers;

import com.github.ezequielarroyo.postservice.dtos.input.UserDTO;
import com.github.ezequielarroyo.postservice.entities.UserSnapshot;
import com.github.ezequielarroyo.postservice.services.IUserSnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/internal/users")
@RequiredArgsConstructor
public class UserSnapshotController {
    private final IUserSnapshotService userSnapshotService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userSnapshotService.createUserSnapshot(user));
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID userId, @RequestBody UserDTO user) {
        return ResponseEntity.ok(userSnapshotService.updateUserSnapshot(user));
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable UUID userId) {
        userSnapshotService.deleteUserSnapshot(userId);
        return ResponseEntity.noContent().build();
    }
}
