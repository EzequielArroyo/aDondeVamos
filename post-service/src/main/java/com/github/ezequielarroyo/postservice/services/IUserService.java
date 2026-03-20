package com.github.ezequielarroyo.postservice.services;

import com.github.ezequielarroyo.postservice.entities.User;

import java.util.UUID;
public interface IUserService {
    User findByUuid(UUID uuid);
}
