package com.github.ezequielarroyo.postservice.clients;

import com.github.ezequielarroyo.postservice.dtos.input.UserDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class UserClientMOCK implements IUserClient {
    @Override
    public UserDTO getUserById(UUID userId) {

        return new UserDTO(
                userId,
            "MockUser",
            "https://example.com/avatar.png"
        );
    }
}
