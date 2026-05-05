package com.github.ezequielarroyo.postservice.clients;

import com.github.ezequielarroyo.postservice.dtos.input.UserDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

public interface IUserClient {
    UserDTO getUserById(UUID userId);
}
