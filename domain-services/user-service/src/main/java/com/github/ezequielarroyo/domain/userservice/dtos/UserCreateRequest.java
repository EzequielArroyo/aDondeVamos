package com.github.ezequielarroyo.domain.userservice.dtos;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;

public record UserCreateRequest(
        @Nonnull
        String username,
        String name,
        String lastName,
        @Nonnull @Email
        String email
) {
}
