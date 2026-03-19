package com.github.ezequielarroyo.postservice.services;

import com.github.ezequielarroyo.postservice.dtos.input.PostCreateRequest;
import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;

import java.util.UUID;

public interface IPostService {
    PostResponse save(PostCreateRequest request, UUID uuid);
    PostResponse findByUuid(UUID uuid);
}
