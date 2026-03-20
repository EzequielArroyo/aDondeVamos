package com.github.ezequielarroyo.postservice.services;

import com.github.ezequielarroyo.postservice.dtos.input.PostCreateRequest;
import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IPostService {
    PostResponse save(PostCreateRequest request, UUID uuid);
    PostResponse findByUuid(UUID uuid);
    Page<PostResponse> findAll(Pageable pageable);
    void joinPost(UUID postUuid, UUID userUuid);
    void leavePost(UUID PostUuid, UUID userUuid);
}
