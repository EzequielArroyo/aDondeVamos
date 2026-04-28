package com.github.ezequielarroyo.postservice.dtos.input;

import java.util.UUID;

public record UserDTO (
    UUID uuid,
    String authId,
    String username,
    String avatar

){}
