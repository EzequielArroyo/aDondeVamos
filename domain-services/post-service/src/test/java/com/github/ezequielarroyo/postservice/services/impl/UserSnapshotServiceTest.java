package com.github.ezequielarroyo.postservice.services.impl;

import com.github.ezequielarroyo.postservice.dtos.input.UserDTO;
import com.github.ezequielarroyo.postservice.entities.UserSnapshot;
import com.github.ezequielarroyo.postservice.exceptions.UserSnapshotNotFoundException;
import com.github.ezequielarroyo.postservice.repositories.IUserSnapshotRepository;
import com.github.ezequielarroyo.postservice.utils.user.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSnapshotServiceTest {

    @Mock
    private IUserSnapshotRepository repository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserSnapshotService service;

    @Captor
    private ArgumentCaptor<UserSnapshot> userCaptor;

    @Test
    void shouldCreateUserSnapshotSuccessfully() {

        UserDTO dto = createUserDTO();

        UserSnapshot entity = createUser(dto.userId());

        givenMapperConvertsDtoToEntity(dto, entity);
        givenRepositorySaves(entity);
        givenMapperConvertsEntityToDto(entity, dto);

        UserDTO result = service.createUserSnapshot(dto);

        UserSnapshot savedUser = captureSavedUser();

        assertUserSnapshotMatchesDto(savedUser, dto);

        assertEquals(dto.userId(), result.userId());
        assertEquals(dto.username(), result.username());
        assertEquals(dto.avatar(), result.avatar());
    }

    @Test
    void shouldThrowExceptionWhenCreateDtoIsNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.createUserSnapshot(null)
        );

        thenUserIsNeverSaved();
    }

    @Test
    void shouldUpdateUserSnapshotSuccessfully() {

        UserDTO dto = createUserDTO();

        UserSnapshot existingUser = createUser(dto.userId());

        givenUserExists(dto.userId(), existingUser);
        givenRepositorySaves(existingUser);
        givenMapperConvertsEntityToDto(existingUser, dto);

        service.updateUserSnapshot(dto);

        thenUserIsSaved(existingUser);

        assertUserSnapshotMatchesDto(existingUser, dto);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {

        UserDTO dto = createUserDTO();

        givenUserDoesNotExist(dto.userId());

        assertThrows(
                UserSnapshotNotFoundException.class,
                () -> service.updateUserSnapshot(dto)
        );

        thenUserIsNeverSaved();
    }

    @Test
    void shouldDeleteUserSnapshotSuccessfully() {

        UUID userId = UUID.randomUUID();

        givenUserAlreadyExists(userId);

        service.deleteUserSnapshot(userId);

        thenUserIsDeleted(userId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {

        UUID userId = UUID.randomUUID();

        givenUserDoesNotAlreadyExist(userId);

        assertThrows(
                UserSnapshotNotFoundException.class,
                () -> service.deleteUserSnapshot(userId)
        );

        thenUserIsNeverDeleted();
    }

    // =========================================================
    // GIVEN
    // =========================================================

    private void givenMapperConvertsDtoToEntity(
            UserDTO dto,
            UserSnapshot entity
    ) {

        when(userMapper.toEntity(dto))
                .thenReturn(entity);
    }

    private void givenMapperConvertsEntityToDto(
            UserSnapshot entity,
            UserDTO dto
    ) {

        when(userMapper.toDTO(entity))
                .thenReturn(dto);
    }

    private void givenRepositorySaves(UserSnapshot user) {

        when(repository.save(user))
                .thenReturn(user);
    }

    private void givenUserExists(UUID userId, UserSnapshot user) {

        when(repository.findByUuid(userId))
                .thenReturn(Optional.of(user));
    }

    private void givenUserDoesNotExist(UUID userId) {

        when(repository.findByUuid(userId))
                .thenReturn(Optional.empty());
    }

    private void givenUserAlreadyExists(UUID userId) {

        when(repository.existsByUuid(userId))
                .thenReturn(true);
    }

    private void givenUserDoesNotAlreadyExist(UUID userId) {

        when(repository.existsByUuid(userId))
                .thenReturn(false);
    }

    // =========================================================
    // THEN
    // =========================================================

    private void thenUserIsSaved(UserSnapshot user) {

        verify(repository).save(user);
    }

    private void thenUserIsDeleted(UUID userId) {

        verify(repository).deleteByUuid(userId);
    }

    private void thenUserIsNeverSaved() {

        verify(repository, never()).save(any());
    }

    private void thenUserIsNeverDeleted() {

        verify(repository, never()).deleteById(any());
    }

    private UserSnapshot captureSavedUser() {

        verify(repository).save(userCaptor.capture());

        return userCaptor.getValue();
    }

    // =========================================================
    // ASSERTS
    // =========================================================

    private void assertUserSnapshotMatchesDto(
            UserSnapshot user,
            UserDTO dto
    ) {

        assertEquals(dto.userId(), user.getUuid());
        assertEquals(dto.username(), user.getUsername());
        assertEquals(dto.avatar(), user.getAvatar());
    }

    // =========================================================
    // FACTORIES
    // =========================================================

    private UserDTO createUserDTO() {

        return UserDTO.builder()
                .userId(UUID.randomUUID())
                .username("TestUserSnapshot")
                .avatar("TestAvatar.png")
                .build();
    }

    private UserSnapshot createUser(UUID userId) {

        return UserSnapshot.create(
                userId,
                "TestUserSnapshot",
                "TestAvatar.png"
        );
    }
}