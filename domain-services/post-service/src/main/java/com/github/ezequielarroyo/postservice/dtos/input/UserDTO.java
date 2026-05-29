package com.github.ezequielarroyo.postservice.dtos.input;

import lombok.Builder;

import java.util.UUID;
@Builder
public record UserDTO (
    UUID userId,
    String username,
    String avatar

){}
