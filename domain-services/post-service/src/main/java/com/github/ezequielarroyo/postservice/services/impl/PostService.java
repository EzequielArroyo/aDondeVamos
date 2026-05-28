package com.github.ezequielarroyo.postservice.services.impl;

import com.github.ezequielarroyo.postservice.dtos.input.PostCreateRequest;
import com.github.ezequielarroyo.postservice.dtos.input.PostUpdateRequest;
import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;
import com.github.ezequielarroyo.postservice.entities.Post;
import com.github.ezequielarroyo.postservice.entities.User;
import com.github.ezequielarroyo.postservice.repositories.IPostRepository;
import com.github.ezequielarroyo.postservice.services.IPostService;
import com.github.ezequielarroyo.postservice.utils.PostMapper;
import com.github.ezequielarroyo.postservice.utils.user.ICurrentUserResolver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService implements IPostService {
    private final IPostRepository postRepository;
    private final ICurrentUserResolver currentUserResolver;
    private final PostMapper postMapper;

    @Override
    public PostResponse createPost(PostCreateRequest request) {
        User owner = currentUserResolver.getCurrentUser();
        log.debug("Creating post for user: {}", owner.getUuid());

        Post post = Post.create(
                request.title(),
                request.location(),
                request.activityDate(),
                request.maxParticipants(),
                owner
        );
        Post savedPost = postRepository.save(post);
        log.info("Post created successfully with uuid: {} by user: {}", savedPost.getUuid(), owner.getUuid());
        return postMapper.toDto(savedPost);
    }

    @Override
    public PostResponse getPostById(UUID uuid) {
        log.debug("Searching post by uuid: {}", uuid);
        return postMapper.toDto(this.getPostByUuid(uuid));
    }

    @Override
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        log.debug("Retrieving page number {} of post with size {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(postMapper::toDto);
    }

    @Transactional
    public void joinPost(UUID postUuid) {
        Post post = this.getPostByUuid(postUuid);
        User user = currentUserResolver.getCurrentUser();
        log.debug("Joining user with uuid: {} to post with uuid: {}", user.getUuid(), postUuid);
        post.addParticipant(user);
        postRepository.save(post);
        log.info("Post joined successfully with uuid: {} by user: {}", postUuid, user.getUuid());
    }

    @Transactional
    public void leavePost(UUID postUuid) {
        Post post = this.getPostByUuid(postUuid);
        User user = currentUserResolver.getCurrentUser();
        log.debug("Leaving user with uuid: {} from post with uuid: {}", user.getUuid(), postUuid);
        post.removeParticipant(user);
        postRepository.save(post);
        log.info("User left successfully with uuid: {} from post with uuid: {}", user.getUuid(), postUuid);
    }

    @Override
    public PostResponse updatePost(UUID PostUuid, PostUpdateRequest request) {
        Post post = this.getPostByUuid(PostUuid);
        log.debug("Updating Post with uuid: {}", PostUuid);
        post.update(request.title(),request.location(),request.activityDate(),request.maxParticipants());
        log.info("Post updated successfully with uuid: {}", PostUuid);
        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    public void deletePost(UUID uuid) {
        log.debug("Deleting Post with uuid: {}", uuid);
        Post post = this.getPostByUuid(uuid);
        post.canceled();
        log.info("Post deleted successfully with uuid: {}", uuid);
        postRepository.save(post);
    }

    private Post getPostByUuid(UUID uuid) {
        return postRepository.findByUuid(uuid)
                .orElseThrow(()-> {
                    log.warn("Post with uuid: {} not found", uuid);
                    return new EntityNotFoundException("Post not found with uuid: " + uuid);
                });

    }
}
