package com.github.ezequielarroyo.domain.userservice.dtos;

import lombok.Builder;

import java.util.UUID;
@Builder
public record UserResponse(
        UUID uuid,
        String username,
        String name,
        String lastName,
        String email,
        String avatar,
        Boolean isCompleted
) {}
