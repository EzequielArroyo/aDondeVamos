package com.github.ezequielarroyo.domain.userservice.services;

import com.github.ezequielarroyo.domain.userservice.auth.IAuthService;
import com.github.ezequielarroyo.domain.userservice.dtos.UserCreateRequest;
import com.github.ezequielarroyo.domain.userservice.dtos.UserResponse;
import com.github.ezequielarroyo.domain.userservice.dtos.UserUpdateRequest;
import com.github.ezequielarroyo.domain.userservice.entities.User;
import com.github.ezequielarroyo.domain.userservice.exceptions.UserAlreadyExistsException;
import com.github.ezequielarroyo.domain.userservice.exceptions.UserNotFoundException;
import com.github.ezequielarroyo.domain.userservice.repositories.IUserRepository;
import com.github.ezequielarroyo.domain.userservice.utils.UserMapper;
import com.github.ezequielarroyo.domain.userservice.utils.UserUpdater;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IAuthService authService;
    @Mock
    private UserMapper mapper;
    @Mock
    private UserUpdater userUpdater;

    @InjectMocks
    private UserService userService;

    // --- Object Creation Helpers ---

    private User createSampleUser(String username, String email) {
        return User.create(username, "John", "Doe", email);
    }

    private UserResponse createSampleUserResponse(User user) {
        return UserResponse.builder()
                .uuid(user.getUuid())
                .username(user.getUsername())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .isCompleted(user.getIsCompleted())
                .build();
    }

    private UserCreateRequest createSampleCreateRequest(String username) {
        return new UserCreateRequest(username, "John", "Doe", username + "@example.com");
    }

    private UserUpdateRequest createSampleUpdateRequest() {
        return new UserUpdateRequest("updatedUser", "JohnUpdate", "DoeUpdate", "update@example.com", "new_avatar.png");
    }

    // --- Stubbing Helpers (Encapsulated "when") ---

    private void givenUserRepositoryReturnsPage(Pageable pageable, Page<User> page) {
        when(userRepository.findAll(pageable)).thenReturn(page);
    }

    private void givenUserExistsInRepo(User user) {
        when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
    }

    private void givenUserExistsByUsername(User user) {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    }

    private void givenUserNotFoundInRepo(UUID uuid) {
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());
    }

    private void givenUserNotFoundByUsername(String username) {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    }

    private void givenCurrentUserIdIs(UUID uuid) {
        when(authService.getCurrentUserId()).thenReturn(uuid);
    }

    private void givenMapperConvertsToResponse(User user, UserResponse response) {
        when(mapper.toUserResponse(user)).thenReturn(response);
    }

    private void givenMapperConvertsToEntity(UserCreateRequest request, User user) {
        when(mapper.toUser(request)).thenReturn(user);
    }

    private void givenUserRepositorySavesUser(User user) {
        when(userRepository.save(any(User.class))).thenReturn(user);
    }

    private void givenUserUpdaterProcessesRequest(User existing, UserUpdateRequest request, User updated) {
        when(userUpdater.updateUser(existing, request)).thenReturn(updated);
    }

    // --- Test Suites ---

    @Nested
    @DisplayName("Tests for getAllUsers")
    class GetAllUsersTests {

        @Test
        @DisplayName("Should return a page of UserResponse when users exist")
        void getAllUsers_Success() {
            Pageable pageable = PageRequest.of(0, 10);
            User user = createSampleUser("jdoe", "jdoe@example.com");
            UserResponse response = createSampleUserResponse(user);
            Page<User> userPage = new PageImpl<>(Collections.singletonList(user));

            givenUserRepositoryReturnsPage(pageable, userPage);
            givenMapperConvertsToResponse(user, response);

            Page<UserResponse> result = userService.getAllUsers(pageable);

            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            assertEquals("jdoe", result.getContent().getFirst().username());
        }

        @Test
        @DisplayName("Should return empty page when no users found")
        void getAllUsers_Empty() {
            Pageable pageable = PageRequest.of(0, 10);
            givenUserRepositoryReturnsPage(pageable, Page.empty());

            Page<UserResponse> result = userService.getAllUsers(pageable);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for getUserByUuid")
    class GetUserByUuidTests {

        @Test
        @DisplayName("Should return UserResponse when UUID exists")
        void getUserByUuid_Success() {
            User user = createSampleUser("test_user", "test@example.com");
            UserResponse response = createSampleUserResponse(user);

            givenUserExistsInRepo(user);
            givenMapperConvertsToResponse(user, response);

            UserResponse result = userService.getUserByUuid(user.getUuid());

            assertEquals(user.getUuid(), result.uuid());
            assertEquals("test_user", result.username());
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when UUID does not exist")
        void getUserByUuid_NotFound() {
            UUID uuid = UUID.randomUUID();
            givenUserNotFoundInRepo(uuid);

            assertThrows(UserNotFoundException.class, () -> userService.getUserByUuid(uuid));
        }
    }

    @Nested
    @DisplayName("Tests for createUser")
    class CreateUserTests {

        @Test
        @DisplayName("Should create user and return UUID successfully")
        void createUser_Success() {
            UserCreateRequest request = createSampleCreateRequest("new_user");
            User userEntity = createSampleUser(request.username(), request.email());

            givenUserNotFoundByUsername(request.username());
            givenMapperConvertsToEntity(request, userEntity);
            givenUserRepositorySavesUser(userEntity);

            UUID resultUuid = userService.createUser(request);

            assertEquals(userEntity.getUuid(), resultUuid);
            verify(userRepository).save(userEntity);
        }

        @Test
        @DisplayName("Should throw UserAlreadyExistsException when username is taken")
        void createUser_AlreadyExists() {
            UserCreateRequest request = createSampleCreateRequest("existing_user");
            User existingUser = createSampleUser(request.username(), request.email());

            givenUserExistsByUsername(existingUser);

            assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(request));
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests for getCurrentUser")
    class GetCurrentUserTests {

        @Test
        @DisplayName("Should return current logged user response")
        void getCurrentUser_Success() {
            User user = createSampleUser("auth_user", "auth@example.com");
            UserResponse response = createSampleUserResponse(user);

            givenCurrentUserIdIs(user.getUuid());
            givenUserExistsInRepo(user);
            givenMapperConvertsToResponse(user, response);

            UserResponse result = userService.getCurrentUser();

            assertNotNull(result);
            assertEquals(user.getUuid(), result.uuid());
        }
    }

    @Nested
    @DisplayName("Tests for updateUser")
    class UpdateUserTests {

        @Test
        @DisplayName("Should update existing user successfully")
        void updateUser_Success() {
            UserUpdateRequest request = createSampleUpdateRequest();
            User existingUser = createSampleUser("old_user", "old@example.com");
            User updatedUser = createSampleUser(request.username(), request.email());

            givenCurrentUserIdIs(existingUser.getUuid());
            givenUserExistsInRepo(existingUser);
            givenUserUpdaterProcessesRequest(existingUser, request, updatedUser);

            userService.updateUser(request);

            verify(userRepository).save(updatedUser);
        }
    }

    @Nested
    @DisplayName("Tests for deleteUser")
    class DeleteUserTests {

        @Test
        @DisplayName("Should delete user when ID exists")
        void deleteUser_Success() {
            User user = createSampleUser("to_delete", "del@example.com");

            givenUserExistsInRepo(user);

            userService.deleteUser(user.getUuid());

            verify(userRepository).delete(user);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent user")
        void deleteUser_NotFound() {
            UUID uuid = UUID.randomUUID();
            givenUserNotFoundInRepo(uuid);

            assertThrows(UserNotFoundException.class, () -> userService.deleteUser(uuid));
            verify(userRepository, never()).delete(any());
        }
    }
}