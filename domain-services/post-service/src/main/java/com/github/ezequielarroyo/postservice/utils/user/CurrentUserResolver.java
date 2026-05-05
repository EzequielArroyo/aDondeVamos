package com.github.ezequielarroyo.postservice.utils.user;

import com.github.ezequielarroyo.postservice.auth.IAuthService;
import com.github.ezequielarroyo.postservice.clients.IUserClient;
import com.github.ezequielarroyo.postservice.dtos.input.UserDTO;
import com.github.ezequielarroyo.postservice.entities.User;
import com.github.ezequielarroyo.postservice.repositories.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrentUserResolver implements ICurrentUserResolver {
    private final IUserRepository userRepository;
    private final IAuthService authService;
    private final IUserClient userClient;
    private final UserMapper userMapper;

    public CurrentUserResolver(IUserRepository userRepository, IAuthService authService, IUserClient userClient, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.userClient = userClient;
        this.userMapper = userMapper;
    }
    public User getCurrentUser() {
        UUID currentUserID = authService.getCurrentUserId();
        return getOrFetchUser(currentUserID);
    }

    private User getOrFetchUser(UUID currentUserId) {
        return userRepository.findByUuid(currentUserId)
                .orElseGet(() -> {
                    UserDTO dto = userClient.getUserById(currentUserId);
                    if (dto == null) {
                        throw new EntityNotFoundException("User not found");
                    }
                    return userRepository.save(userMapper.toEntity(dto));
                });
    }
}
