package com.github.ezequielarroyo.domain.commonexceptions;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;
@Builder
public record ErrorResponse(
        Integer status,
        String message,
        LocalDateTime timestamp,
        Map<String, String> details
) {}
