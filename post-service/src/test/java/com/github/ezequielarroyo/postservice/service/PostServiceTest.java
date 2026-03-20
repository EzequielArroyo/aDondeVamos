package com.github.ezequielarroyo.postservice.service;

import com.github.ezequielarroyo.postservice.dtos.input.PostCreateRequest;
import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;
import com.github.ezequielarroyo.postservice.entities.Location;
import com.github.ezequielarroyo.postservice.entities.Post;
import com.github.ezequielarroyo.postservice.entities.PostStatus;
import com.github.ezequielarroyo.postservice.entities.User;
import com.github.ezequielarroyo.postservice.repositories.IPostRepository;
import com.github.ezequielarroyo.postservice.services.IUserService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock private IPostRepository postRepository;
    @Mock private IUserService userService;

    @Spy private PostMapper postMapper = new PostMapper();

    @InjectMocks private PostService postService;

    private User owner;
    private UUID userUuid;
    private Post savedPost;

    @BeforeEach
    void setUp() {
        userUuid = UUID.randomUUID();
        owner = createUser("owner");
        savedPost = createPost(owner);
    }

    // =========================
    // 🧪 SAVE
    // =========================

    @Test
    @DisplayName("Should save post and return mapped response")
    void savePost_ShouldReturnSavedPostResponse() {
        PostCreateRequest request = createValidRequest();

        mockUserFound(userUuid, owner);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostResponse result = postService.save(request, userUuid);

        assertNotNull(result);
        assertEquals(savedPost.getUuid(), result.uuid());
        assertEquals("Go to the park", result.title());

        verify(postMapper).toDto(savedPost);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("Should throw exception when user owner is not found")
    void savePost_UserNotFound_ThrowsException() {
        PostCreateRequest request = createValidRequest();

        mockUserNotFound(userUuid);

        assertThrows(EntityNotFoundException.class, () ->
                postService.save(request, userUuid)
        );

        verifyNoInteractions(postRepository);
    }

    // =========================
    // 🧪 FIND
    // =========================

    @Test
    @DisplayName("Should return post when searching by UUID")
    void findByUuid_ShouldReturnPostResponse() {
        mockPostFound(savedPost);

        PostResponse result = postService.findByUuid(savedPost.getUuid());

        assertNotNull(result);
        assertEquals(savedPost.getTitle(), result.title());
    }

    @Test
    @DisplayName("Should throw when post not found")
    void findByUuid_NotFound_ThrowsException() {
        mockPostNotFound();

        assertThrows(EntityNotFoundException.class, () ->
                postService.findByUuid(UUID.randomUUID())
        );
    }

    // =========================
    // 🧪 JOIN
    // =========================

    @Test
    @DisplayName("Should create a participant when user joins")
    void joinPost_Success() {
        User user = createUser("participant");

        mockPostFound(savedPost);
        mockUserFound(userUuid, user);

        postService.joinPost(savedPost.getUuid(), userUuid);

        assertEquals(1, savedPost.getParticipants().size());
        verify(postRepository).save(savedPost);
    }

    @Test
    @DisplayName("Should throw when post does not exist")
    void joinPost_PostNotFound() {
        mockPostNotFound();

        assertThrows(EntityNotFoundException.class, () ->
                postService.joinPost(UUID.randomUUID(), userUuid)
        );
    }

    @Test
    @DisplayName("Should throw when user does not exist")
    void joinPost_UserNotFound() {
        mockPostFound(savedPost);
        mockUserNotFound(userUuid);

        assertThrows(EntityNotFoundException.class, () ->
                postService.joinPost(savedPost.getUuid(), userUuid)
        );
    }

    @Test
    @DisplayName("Should throw when user already joined")
    void joinPost_UserAlreadyParticipant() {
        User user = createUser("runner");

        savedPost.addParticipant(user);

        mockPostFound(savedPost);
        mockUserFound(userUuid, user);

        assertThrows(IllegalStateException.class, () ->
                postService.joinPost(savedPost.getUuid(), userUuid)
        );
    }

    @Test
    @DisplayName("Should throw when post is full")
    void joinPost_PostFull() {
        for (int i = 0; i < savedPost.getMaxParticipants(); i++) {
            savedPost.addParticipant(createUser("user" + i));
        }
        User newUser = createUser("newUser");

        mockPostFound(savedPost);
        mockUserFound(userUuid, newUser);

        assertThrows(IllegalStateException.class, () ->
                postService.joinPost(savedPost.getUuid(), userUuid)
        );
    }

    // =========================
    // 🧪 LEAVE
    // =========================

    @Test
    @DisplayName("Should add and then remove participant")
    void joinAndLeavePost_Success() {
        User user = createUser("runner");

        mockPostFound(savedPost);
        mockUserFound(userUuid, user);

        postService.joinPost(savedPost.getUuid(), userUuid);
        postService.leavePost(savedPost.getUuid(), userUuid);

        assertTrue(savedPost.getParticipants().isEmpty());
        verify(postRepository, times(2)).save(savedPost);
    }

    @Test
    @DisplayName("Should throw when leaving non-existing post")
    void leavePost_PostNotFound() {
        mockPostNotFound();

        assertThrows(EntityNotFoundException.class, () ->
                postService.leavePost(UUID.randomUUID(), userUuid)
        );
    }

    @Test
    @DisplayName("Should throw when user not found")
    void leavePost_UserNotFound() {
        mockPostFound(savedPost);
        mockUserNotFound(userUuid);

        assertThrows(EntityNotFoundException.class, () ->
                postService.leavePost(savedPost.getUuid(), userUuid)
        );
    }

    @Test
    @DisplayName("Should throw when user is not participant")
    void leavePost_UserNotParticipant() {
        User user = createUser("runner");

        mockPostFound(savedPost);
        mockUserFound(userUuid, user);

        assertThrows(EntityNotFoundException.class, () ->
                postService.leavePost(savedPost.getUuid(), userUuid)
        );
    }

    @Test
    @DisplayName("Should reopen post when leaving and it was full")
    void leavePost_ReopensPost() {
        User user = createUser("runner");

        savedPost.addParticipant(user);
        savedPost.setStatus(PostStatus.FULL);

        mockPostFound(savedPost);
        mockUserFound(userUuid, user);

        postService.leavePost(savedPost.getUuid(), userUuid);

        assertEquals(PostStatus.OPEN, savedPost.getStatus());
    }

    // =========================
    // 🧩 HELPERS
    // =========================

    private User createUser(String name) {
        return User.create(name, "img.png");
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

    private void mockUserFound(UUID uuid, User user) {
        when(userService.findByUuid(uuid)).thenReturn(user);
    }

    private void mockUserNotFound(UUID uuid) {
        when(userService.findByUuid(uuid))
                .thenThrow(new EntityNotFoundException());
    }

    private void mockPostFound(Post post) {
        when(postRepository.findByUuid(post.getUuid()))
                .thenReturn(Optional.of(post));
    }

    private void mockPostNotFound() {
        when(postRepository.findByUuid(any()))
                .thenReturn(Optional.empty());
    }

    private PostCreateRequest createValidRequest() {
        return new PostCreateRequest(
                "Go to the park",
                Location.create(82.3, 90.0),
                LocalDateTime.now().plusDays(1),
                5
        );
    }
}