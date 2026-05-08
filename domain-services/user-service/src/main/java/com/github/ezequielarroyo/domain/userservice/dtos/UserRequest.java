package com.github.ezequielarroyo.domain.userservice.dtos;
//TODO: add validations
public record UserRequest(
        String username,
        String name,
        String lastname,
        String email,
        String avatar
) {}
