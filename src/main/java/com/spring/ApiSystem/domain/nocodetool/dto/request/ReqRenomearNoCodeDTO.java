package com.spring.ApiSystem.domain.nocodetool.dto.request;

import java.io.Serializable;

public record ReqRenomearNoCodeDTO(
        String modificationName
) implements Serializable {
}

