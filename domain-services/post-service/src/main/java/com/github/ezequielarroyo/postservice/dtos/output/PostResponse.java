package com.github.ezequielarroyo.postservice.dtos.output;

import com.github.ezequielarroyo.postservice.entities.Location;
import com.github.ezequielarroyo.postservice.entities.Participant;
import com.github.ezequielarroyo.postservice.entities.PostStatus;
import com.github.ezequielarroyo.postservice.entities.UserSnapshot;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record PostResponse(
        UUID uuid,
        Boolean active,
        String title,
        Location location,
        LocalDateTime activityDate,
        Integer maxParticipants,
        PostStatus status,
        UserSnapshot owner,
        List<Participant> participants,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {}
