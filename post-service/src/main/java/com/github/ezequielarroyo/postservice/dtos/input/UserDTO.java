package com.github.ezequielarroyo.postservice.dtos.input;

import java.util.UUID;

public record UserDTO (
    UUID userId,
    String username,
    String avatar

){}
