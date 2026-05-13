package com.github.ezequielarroyo.domain.userservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(
        @NotNull(message = "Username is required")
        @Min(message = "Username must be at least 3 characters long", value = 3)
        String username,
        @NotNull(message = "Name is required")
        @Min(message = "Name must be at least 2 characters long", value = 2)
        String name,
        @NotNull(message = "Last name is required")
        @Min(message = "Last name must be at least 2 characters long", value = 2)
        String lastname,
        @NotNull(message = "Email is required")
        @Email(message = "Email is not valid")
        String email,
        String avatar
) {}
