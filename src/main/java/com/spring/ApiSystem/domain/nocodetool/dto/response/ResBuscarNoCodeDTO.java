package com.spring.ApiSystem.domain.nocodetool.dto.response;

import com.spring.ApiSystem.domain.nocodetool.NoCode;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResBuscarNoCodeDTO(
        UUID id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    public ResBuscarNoCodeDTO(NoCode content) {
        this(content.getId(), content.getContent(), content.getCreatedAt(), content.getUpdatedAt());
    }
}
