package com.github.ezequielarroyo.domain.userservice.dtos;

import lombok.Builder;

@Builder
public record UserProfileData(
        String username,
        String firstname,
        String lastname,
        String email,
        String avatar
) {
}
