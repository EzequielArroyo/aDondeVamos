package com.github.ezequielarroyo.postservice.utils;

import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;
import com.github.ezequielarroyo.postservice.entities.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

public PostResponse toDto(Post post) {
    return PostResponse.builder()
            .uuid(post.getUuid())
            .active(post.getActive())
            .title(post.getTitle())
            .location(post.getLocation())
            .activityDate(post.getActivityDate())
            .maxParticipants(post.getMaxParticipants())
            .status(post.getStatus())
            .owner(post.getOwner())
            .participants(post.getParticipants())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .deletedAt(post.getDeletedAt())
            .build();
}
}
