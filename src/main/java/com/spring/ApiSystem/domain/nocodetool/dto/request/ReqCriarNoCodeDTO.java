package com.spring.ApiSystem.domain.nocodetool.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.spring.ApiSystem.domain.nocodetool.NoCode;

public record ReqCriarNoCodeDTO(
        UUID id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    public ReqCriarNoCodeDTO(NoCode content) {
        this(content.getId(), content.getContent(), content.getCreatedAt(), content.getUpdatedAt());
    }
}
