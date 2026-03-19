package com.github.ezequielarroyo.postservice.services.impl;

import com.github.ezequielarroyo.postservice.dtos.input.PostCreateRequest;
import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;
import com.github.ezequielarroyo.postservice.entities.Post;
import com.github.ezequielarroyo.postservice.entities.User;
import com.github.ezequielarroyo.postservice.repositories.IPostRepository;
import com.github.ezequielarroyo.postservice.repositories.IUserRepository;
import com.github.ezequielarroyo.postservice.services.IPostService;
import com.github.ezequielarroyo.postservice.utils.PostMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostService implements IPostService {
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final PostMapper postMapper;

    public PostService(IPostRepository postRepository, IUserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostResponse save(PostCreateRequest request, UUID uuid) {
        User owner = userRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntityNotFoundException("User not found with uuid: " + uuid));
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
        Post post = postRepository.findByUuid(uuid)
                .orElseThrow(()-> new EntityNotFoundException("Post not found with uuid: " + uuid));
        return postMapper.toDto(post);
    }
}
