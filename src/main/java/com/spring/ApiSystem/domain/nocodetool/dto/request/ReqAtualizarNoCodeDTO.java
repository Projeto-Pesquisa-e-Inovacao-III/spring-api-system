package com.spring.ApiSystem.domain.nocodetool.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.spring.ApiSystem.domain.nocodetool.NoCode;

public record ReqAtualizarNoCodeDTO(
        UUID id,
        String modificationName,
        String description,
        String content
) {
}

