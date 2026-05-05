package com.github.ezequielarroyo.postservice.dtos.input;

import com.github.ezequielarroyo.postservice.entities.Location;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record PostUpdateRequest (
    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title should be up to 50 characters")
    String title,

    @NotNull(message = "Location is required")
    Location location,

    @NotNull(message = "Activity date is required")
    @Future(message = "Activity date must be in the future")
    LocalDateTime activityDate,

    @NotNull(message = "Max participants is required")
    @Min(value = 2, message = "Max participants must be at least 2")
    @Max(value = 24, message = "Max participants must be at most 24")
    Integer maxParticipants
){}
