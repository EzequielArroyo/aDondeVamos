package com.github.ezequielarroyo.postservice.services;

import com.github.ezequielarroyo.postservice.dtos.input.PostCreateRequest;
import com.github.ezequielarroyo.postservice.dtos.input.PostUpdateRequest;
import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IPostService {
    PostResponse createPost(PostCreateRequest request);
    PostResponse getPostById(UUID uuid);
    Page<PostResponse> getAllPosts(Pageable pageable);
    void joinPost(UUID postUuid);
    void leavePost(UUID PostUuid);
    PostResponse updatePost(UUID PostUuid, PostUpdateRequest request);
}
