package com.github.ezequielarroyo.domain.userservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
        @NotNull(message = "Username is required")
        String username,
        String name,
        String lastName,
        @NotNull(message = "Email is required")
        @Email(message = "Email is not valid")
        String email
) {
}
