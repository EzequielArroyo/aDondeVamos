package com.github.ezequielarroyo.postservice.service;

import com.github.ezequielarroyo.postservice.dtos.input.PostCreateRequest;
import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;
import com.github.ezequielarroyo.postservice.entities.Location;
import com.github.ezequielarroyo.postservice.entities.Post;
import com.github.ezequielarroyo.postservice.entities.User;
import com.github.ezequielarroyo.postservice.repositories.IPostRepository;
import com.github.ezequielarroyo.postservice.repositories.IUserRepository;
import com.github.ezequielarroyo.postservice.services.impl.PostService;
import com.github.ezequielarroyo.postservice.utils.PostMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock private IPostRepository postRepository;
    @Mock private IUserRepository userRepository;

    // Usamos @Spy para que sea la implementación REAL la que se ejecute
    @Spy
    private PostMapper postMapper = new PostMapper();

    @InjectMocks private PostService postService;

    // Atributos de clase para que estén disponibles en todos los @Test
    private User owner;
    private UUID userUuid;
    private Post savedPost;

    @BeforeEach
    void setUp() {
        Location location = Location.create(82.3, 90.0);
        LocalDateTime time = LocalDateTime.of(2026, 11, 19, 14, 0);

        userUuid = UUID.randomUUID();
        owner = User.create("userTest", null);

        savedPost = Post.create("Go to the park", location, time, 5, owner);
        savedPost.setId(1L);
    }

    @Test
    @DisplayName("Should save post and return mapped response")
    void savePost_ShouldReturnSavedPostResponse() {

        PostCreateRequest request = createValidRequest();

        // mocks configs
        when(userRepository.findByUuid(userUuid)).thenReturn(Optional.of(owner));
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // test
        PostResponse result = postService.save(request, userUuid);

        // Assert
        assertNotNull(result);
        assertEquals(savedPost.getUuid(), result.uuid()); // El UUID lo generó el mapper real o venía del post
        assertEquals("Go to the park", result.title());

        verify(postMapper).toDto(savedPost);
        verify(postRepository).save(argThat(p ->
                p.getOwner().equals(owner) && p.getTitle().equals("Go to the park")
        ));
    }
    @Test
    @DisplayName("Should throw exception when user owner is not found")
    void savePost_UserNotFound_ThrowsException() {
        // Arrange
        UUID randomUuid = UUID.randomUUID();
        PostCreateRequest request = createValidRequest();

        // Simulamos que el repositorio devuelve vacío
        when(userRepository.findByUuid(randomUuid)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                postService.save(request, randomUuid)
        );
        assertNotNull(exception);
        // VERIFICACIÓN CRÍTICA: Aseguramos que NUNCA se intentó guardar nada
        verifyNoInteractions(postRepository);
        verifyNoInteractions(postMapper);
    }

    private PostCreateRequest createValidRequest() {
        return new PostCreateRequest(
                "Go to the park",
                Location.create(82.3, 90.0),
                LocalDateTime.now().plusDays(1),
               5
        );
    }
    @Test
    @DisplayName("Should return post when searching by UUID")
    void findByUuid_ShouldReturnPostResponse() {
        UUID savedPostUuid = savedPost.getUuid();
        // Arrange
        when(postRepository.findByUuid(savedPostUuid)).thenReturn(Optional.of(savedPost));

        // Act
        PostResponse result = postService.findByUuid(savedPostUuid);

        // Assert
        assertNotNull(result);
        assertEquals(savedPost.getTitle(), result.title());

        verify(postRepository).findByUuid(savedPostUuid);
    }
}