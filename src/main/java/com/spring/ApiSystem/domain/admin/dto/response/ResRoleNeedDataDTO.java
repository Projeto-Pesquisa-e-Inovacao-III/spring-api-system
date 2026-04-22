package com.spring.ApiSystem.domain.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

public record ResRoleNeedDataDTO(
        boolean needData,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        HashMap<String, String> needFields
) {
    public ResRoleNeedDataDTO(boolean needData) {
        this(needData, null);
    }
}
