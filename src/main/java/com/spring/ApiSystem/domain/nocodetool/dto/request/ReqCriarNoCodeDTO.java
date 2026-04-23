package com.spring.ApiSystem.domain.nocodetool.dto.request;

import com.spring.ApiSystem.domain.nocodetool.NoCode;

import java.time.LocalDateTime;
import java.util.UUID;

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
