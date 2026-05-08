package com.github.ezequielarroyo.domain.userservice.dtos;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

public record UserUpdateRequest(
        @Nonnull
        @Min(6)
        String username,
        @Nonnull
        @Min(3)
        String name,
        @Nonnull
        String lastname,
        @Nonnull
        @Email
        String email,
        String avatar
) {}
