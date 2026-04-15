package com.spring.ApiSystem.domain.nocodetool.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.spring.ApiSystem.domain.nocodetool.NoCode;

public record ReqAtualizarNoCodeDTO(
        UUID id,
        String content,
        LocalDateTime updatedAt

) {
    public ReqAtualizarNoCodeDTO(NoCode content) {
        this(content.getId(), content.getContent(), content.getUpdatedAt());
    }
}
