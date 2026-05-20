package com.github.ezequielarroyo.domain.userservice.dtos;

import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(message = "Username must be at least 3 characters long", min = 3)
        String username,
        @Size(message = "Name must be at least 2 characters long", min = 3)
        String firstname,
        @Size(message = "Last name must be at least 2 characters long", min = 3)
        String lastname,
        String avatar
) {}
