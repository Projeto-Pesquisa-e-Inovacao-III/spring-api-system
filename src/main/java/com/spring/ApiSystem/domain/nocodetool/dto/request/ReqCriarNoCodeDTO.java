package com.spring.ApiSystem.domain.nocodetool.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.spring.ApiSystem.domain.nocodetool.NoCode;

public record ReqCriarNoCodeDTO(
        UUID id,
        String modificationName,
        String description,
        String content,
        LocalDateTime createdAt
) {
    public ReqCriarNoCodeDTO(NoCode content) {
        this(content.getId(), content.getModificationName(), content.getDescription(), content.getContent(), content.getCreatedAt());
    }
}
