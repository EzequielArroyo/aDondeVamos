package com.github.ezequielarroyo.postservice.services;

import com.github.ezequielarroyo.postservice.dtos.input.PostCreateRequest;
import com.github.ezequielarroyo.postservice.dtos.input.PostUpdateRequest;
import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;
import com.github.ezequielarroyo.postservice.entities.Location;
import com.github.ezequielarroyo.postservice.entities.Post;
import com.github.ezequielarroyo.postservice.entities.PostStatus;
import com.github.ezequielarroyo.postservice.entities.User;
import com.github.ezequielarroyo.postservice.repositories.IPostRepository;
import com.github.ezequielarroyo.postservice.services.imp.PostService;
import com.github.ezequielarroyo.postservice.utils.PostMapper;
import com.github.ezequielarroyo.postservice.utils.user.ICurrentUserResolver;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock private IPostRepository postRepository;
    @Mock private ICurrentUserResolver currentUserResolver;
    @Spy private PostMapper postMapper = new PostMapper();

    @InjectMocks private PostService postService;

    private User owner;
    private Post savedPost;

    @BeforeEach
    void setUp() {
        owner = createUser("owner");
        savedPost = createPost(owner);
    }
    //SAVE
    @Test
    @DisplayName("Should save post and return mapped response")
    void savePost_ShouldReturnSavedPostResponse() {
        PostCreateRequest request = aValidRequest();
        givenRepositorySavesSuccessfully(savedPost);
        givenUserIsAuthenticated(owner);

        PostResponse result = postService.createPost(request);

        assertNotNull(result);
        assertEquals(savedPost.getUuid(), result.uuid());
        assertEquals("Go to the park", result.title());
        verify(postMapper).toDto(savedPost);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("Should throw exception when user owner is not found")
    void savePost_UserNotFound_ThrowsException() {
        PostCreateRequest request = aValidRequest();
        givenUserWillNotBeFound();
        assertThrows(EntityNotFoundException.class, () ->
                postService.createPost(request)
        );
        verifyNoInteractions(postRepository);
    }


    //FIND
    @Test
    @DisplayName("Should return post when searching by UUID")
    void findByUuid_ShouldReturnPostResponse() {
        givenPostExists(savedPost);

        PostResponse result = postService.getPostById(savedPost.getUuid());

        assertNotNull(result);
        assertEquals(savedPost.getTitle(), result.title());
    }

    @Test
    @DisplayName("Should throw when post not found")
    void findByUuid_NotFound_ThrowsException() {
        givenPostDoesNotExist();

        assertThrows(EntityNotFoundException.class, () ->
                postService.getPostById(UUID.randomUUID())
        );
    }

    //JOIN
    @Nested
    @DisplayName("Join Post Tests")
    class JoinPostTests {

        private User participant;

        @BeforeEach
        void setUp() {
            participant = createUser("participant");
        }

        @Test
        @DisplayName("Should create a participant when user joins")
        void joinPost_Success() {
            givenPostExists(savedPost);
            givenUserIsAuthenticated(participant);

            postService.joinPost(savedPost.getUuid());

            assertEquals(1, savedPost.getParticipants().size());
            verify(postRepository).save(savedPost);
        }

        @Test
        @DisplayName("Should throw when post does not exist")
        void joinPost_PostNotFound() {
            givenPostDoesNotExist();

            assertThrows(EntityNotFoundException.class, () ->
                    postService.joinPost(UUID.randomUUID())
            );
        }

        @Test
        @DisplayName("Should throw when user does not exist")
        void joinPost_UserNotFound() {
            givenPostExists(savedPost);
            givenUserWillNotBeFound();

            assertThrows(EntityNotFoundException.class, () ->
                    postService.joinPost(savedPost.getUuid())
            );
            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw when user already joined")
        void joinPost_UserAlreadyParticipant() {
            // GIVEN: El usuario ya está dentro
            savedPost.addParticipant(participant);
            givenPostExists(savedPost);
            givenUserIsAuthenticated(participant);

            assertThrows(IllegalStateException.class, () ->
                    postService.joinPost(savedPost.getUuid())
            );
        }

        @Test
        @DisplayName("Should throw when post is full")
        void joinPost_PostFull() {
            for (int i = 0; i < savedPost.getMaxParticipants(); i++) {
                savedPost.addParticipant(createUser("user" + i));
            }

            givenPostExists(savedPost);
            givenUserIsAuthenticated(participant);

            assertThrows(IllegalStateException.class, () ->
                    postService.joinPost(savedPost.getUuid())
            );
        }
    }



    //LEAVE
    @Nested
    @DisplayName("Leave Post Tests")
    class LeavePostTests {

        private User runner;

        @BeforeEach
        void setUp() {
            runner = createUser("runner");
            savedPost.getParticipants().clear();
            savedPost.setStatus(PostStatus.OPEN);
        }

        @Test
        @DisplayName("Should remove participant successfully")
        void leavePost_Success() {
            savedPost.addParticipant(runner);//Given:the user is joined.
            givenPostExists(savedPost);
            givenUserIsAuthenticated(runner);

            postService.leavePost(savedPost.getUuid());

            assertTrue(savedPost.getParticipants().isEmpty());
            verify(postRepository).save(savedPost);
        }

        @Test
        @DisplayName("Should throw when leaving non-existing post")
        void leavePost_PostNotFound() {
            givenPostDoesNotExist();

            assertThrows(EntityNotFoundException.class, () ->
                    postService.leavePost(UUID.randomUUID())
            );
            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw when user not found")
        void leavePost_UserNotFound() {
            givenPostExists(savedPost);
            givenUserWillNotBeFound();

            assertThrows(EntityNotFoundException.class, () ->
                    postService.leavePost(savedPost.getUuid())
            );
            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw when user is not a participant")
        void leavePost_UserNotParticipant() {

            givenPostExists(savedPost);
            givenUserIsAuthenticated(runner);

            assertThrows(IllegalStateException.class, () ->
                    postService.leavePost(savedPost.getUuid())
            );
            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should reopen post when leaving and it was full")
        void leavePost_ReopensPost() {
            //GIVEN:Post full and user joined
            savedPost.addParticipant(runner);
            savedPost.setStatus(PostStatus.FULL);
            givenPostExists(savedPost);
            givenUserIsAuthenticated(runner);

            postService.leavePost(savedPost.getUuid());

            assertEquals(PostStatus.OPEN, savedPost.getStatus());
            assertTrue(savedPost.getParticipants().isEmpty());
            verify(postRepository).save(savedPost);
        }
    }
    @Test
    void shouldUpdatePostSuccessfully(){
        PostUpdateRequest request = new PostUpdateRequest(
                "Nuevo título",
                Location.create(80.0,90.0),
                LocalDateTime.now().plusDays(1),
                10
        );
        givenPostExists(savedPost);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostResponse response = postService.updatePost(savedPost.getUuid(), request);

        // then
        assertEquals("Nuevo título", response.title());
        verify(postMapper).toDto(savedPost);
    }
    @Test
    void shouldThrowExceptionWhenPostNotFound() {
        UUID uuid = UUID.randomUUID();
        PostUpdateRequest request = new PostUpdateRequest(
                "Nuevo título",
                Location.create(80.0,90.0),
                LocalDateTime.now().plusDays(1),
                10
        );

        givenPostDoesNotExist();
        // when / then
        assertThrows(RuntimeException.class, () ->
                postService.updatePost(uuid, request)
        );
    }
    @Test
    @DisplayName("Should keep existing data when update request fields are null")
    void shouldNotUpdateFieldsWhenNull() {
        // Given
        PostUpdateRequest request = new PostUpdateRequest(null, null, null, null);

        // Guardamos los valores originales para comparar
        String originalTitle = savedPost.getTitle();
        Location originalLocation = savedPost.getLocation();

        givenPostExists(savedPost);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PostResponse result = postService.updatePost(savedPost.getUuid(), request);

        // Then
        assertNotNull(result);
        assertEquals(originalTitle, result.title(), "Title should not have changed");
        assertEquals(originalLocation.getLatitude(), savedPost.getLocation().getLatitude(), "Location should remain the same");

        verify(postRepository).save(savedPost);
    }
    @Test
    @DisplayName("Should throw IllegalStateException when reducing capacity below current participants count")
    void shouldThrowExceptionWhenReducingMaxParticipantsBelowCurrent() {
        for (int i = 0; i < 4; i++) {
            savedPost.addParticipant(createUser("User" + i));
        }

        PostUpdateRequest request = new PostUpdateRequest(
                null,
                null,
                null,
                3
        );

        givenPostExists(savedPost);

        assertThrows(IllegalStateException.class, () ->
                postService.updatePost(savedPost.getUuid(), request)
        );

        verify(postRepository, never()).save(any());
    }
    @Test
    @DisplayName("Should change status to OPEN when maxParticipants increases and post was FULL")
    void shouldUpdateStatusToOpenWhenMaxParticipantsChanges() {
        savedPost.setStatus(PostStatus.FULL);
        savedPost.setMaxParticipants(5);
        for (int i = 0; i < 5; i++) {
            savedPost.addParticipant(createUser("User" + i));
        }

        PostUpdateRequest request = new PostUpdateRequest(
                null, null, null, 10
        );

        givenPostExists(savedPost);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        postService.updatePost(savedPost.getUuid(), request);

        assertEquals(PostStatus.OPEN, savedPost.getStatus(),
                "The post should be OPEN after increasing capacity");
        assertEquals(10, savedPost.getMaxParticipants());
        verify(postRepository).save(savedPost);
    }

    //HELPERS
    private User createUser(String username) {
        return User.create(UUID.randomUUID(),username, "img.png");
    }

    private Post createPost(User owner) {
        Post post = Post.create(
                "Go to the park",
                Location.create(82.3, 90.0),
                LocalDateTime.of(2026, 11, 19, 14, 0),
                5,
                owner
        );
        post.setId(1L);
        return post;
    }

    private void givenUserIsAuthenticated(User currentUser) {
        when(currentUserResolver.getCurrentUser())
                .thenReturn(currentUser);
    }
    private void givenRepositorySavesSuccessfully(Post savedPost){
        when(postRepository.save(any(Post.class))).
                thenReturn(savedPost);
    }
    private void givenUserWillNotBeFound() {
        when(currentUserResolver.getCurrentUser())
                .thenThrow(new EntityNotFoundException("User not found"));
    }
    private void givenPostExists(Post post) {
        when(postRepository.findByUuid(post.getUuid()))
                .thenReturn(Optional.of(post));
    }
    private void givenPostDoesNotExist() {
        when(postRepository.findByUuid(any()))
                .thenReturn(Optional.empty());
    }
    private PostCreateRequest aValidRequest() {
        return new PostCreateRequest(
                "Go to the park",
                Location.create(82.3, 90.0),
                LocalDateTime.now().plusDays(1),
                5
        );
    }
}