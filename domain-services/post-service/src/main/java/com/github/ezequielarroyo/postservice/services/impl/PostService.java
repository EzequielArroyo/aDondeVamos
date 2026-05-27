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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final IPostRepository postRepository;
    private final ICurrentUserResolver currentUserResolver;
    private final PostMapper postMapper;

    @Override
    public PostResponse createPost(PostCreateRequest request) {
        User owner = currentUserResolver.getCurrentUser();
        if (owner == null) {
            throw new EntityNotFoundException("User not found");
        }
        Post post = Post.create(
                request.title(),
                request.location(),
                request.activityDate(),
                request.maxParticipants(),
                owner
        );
        Post savedPost = postRepository.save(post);
        return postMapper.toDto(savedPost);
    }

    @Override
    public PostResponse getPostById(UUID uuid) {
        return postMapper.toDto(this.getPostByUuid(uuid));
    }

    @Override
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(postMapper::toDto);
    }

    @Transactional
    public void joinPost(UUID postUuid) {
        Post post = this.getPostByUuid(postUuid);
        User user = currentUserResolver.getCurrentUser();
        post.addParticipant(user);
        postRepository.save(post);
    }

    @Transactional
    public void leavePost(UUID postUuid) {
        Post post = this.getPostByUuid(postUuid);
        User user = currentUserResolver.getCurrentUser();
        post.removeParticipant(user);
        postRepository.save(post);
    }

    @Override
    public PostResponse updatePost(UUID PostUuid, PostUpdateRequest request) {
        Post post = this.getPostByUuid(PostUuid);
        post.update(request.title(),request.location(),request.activityDate(),request.maxParticipants());
        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    public void deletePost(UUID uuid) {
        Post post = this.getPostByUuid(uuid);
        post.canceled();
        postRepository.save(post);
    }

    private Post getPostByUuid(UUID uuid) {
        return postRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntityNotFoundException("Post not found with uuid: " + uuid));

    }
}
