package com.github.ezequielarroyo.domain.userservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotNull(message = "Username is required")
        @Size(message = "Username must be at least 3 characters long", min = 3)
        String username,
        @NotNull(message = "Name is required")
        @Size(message = "Name must be at least 2 characters long", min = 3)
        String firstname,
        @NotNull(message = "Last name is required")
        @Size(message = "Last name must be at least 2 characters long", min = 3)
        String lastname,
        @Email(message = "Email is not valid")
        String email,
        String avatar
) {
}
