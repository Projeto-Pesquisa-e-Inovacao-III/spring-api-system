package com.spring.ApiSystem.domain.nocodetool.dto.response;

import com.spring.ApiSystem.domain.nocodetool.NoCode;

import java.io.Serializable;
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
) implements Serializable {
}

