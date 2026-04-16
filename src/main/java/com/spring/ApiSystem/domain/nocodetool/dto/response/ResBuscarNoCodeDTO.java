package com.spring.ApiSystem.domain.nocodetool.dto.response;

import com.spring.ApiSystem.domain.nocodetool.NoCode;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResBuscarNoCodeDTO(
        UUID id,
        String content,
        String modificationName,
        String description,
        LocalDateTime createdAt,
        LocalDateTime restoredAt,
        UUID restoredFromId
) {
    public ResBuscarNoCodeDTO(NoCode content) {
        this(content.getId(), content.getContent(), content.getModificationName(), content.getDescription(), content.getCreatedAt(), content.getRestoredAt(), content.getRestoredFromId());
    }
}
