package com.github.ezequielarroyo.postservice.services.impl;

import com.github.ezequielarroyo.postservice.dtos.input.PostCreateRequest;
import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;
import com.github.ezequielarroyo.postservice.entities.Post;
import com.github.ezequielarroyo.postservice.entities.User;
import com.github.ezequielarroyo.postservice.repositories.IPostRepository;
import com.github.ezequielarroyo.postservice.services.IPostService;
import com.github.ezequielarroyo.postservice.services.IUserService;
import com.github.ezequielarroyo.postservice.utils.PostMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PostService implements IPostService {
    private final IPostRepository postRepository;
    private final IUserService userService;
    private final PostMapper postMapper;

    public PostService(IPostRepository postRepository, IUserService userService, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.postMapper = postMapper;
    }

    @Override
    public PostResponse save(PostCreateRequest request, UUID uuid) {
        User owner = userService.findByUuid(uuid);
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
    public PostResponse findByUuid(UUID uuid) {
        return postMapper.toDto(this.getPostByUuid(uuid));
    }

    @Override
    public Page<PostResponse> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(postMapper::toDto);
    }

    @Transactional
    public void joinPost(UUID postUuid, UUID userUuid) {
        Post post = this.getPostByUuid(postUuid);
        User user = userService.findByUuid(userUuid);
        post.addParticipant(user);
        postRepository.save(post);
    }

    @Transactional
    public void leavePost(UUID postUuid, UUID userUuid) {
        Post post = this.getPostByUuid(postUuid);
        User user = userService.findByUuid(userUuid);
        post.removeParticipant(user);
        postRepository.save(post);
    }
    private Post getPostByUuid(UUID uuid) {
        return postRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntityNotFoundException("Post not found with uuid: " + uuid));

    }
}
