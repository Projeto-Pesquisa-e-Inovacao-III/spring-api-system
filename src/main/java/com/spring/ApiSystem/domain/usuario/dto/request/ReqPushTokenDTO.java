package com.spring.ApiSystem.domain.usuario.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReqPushTokenDTO(
        @NotBlank(message = "pushToken é obrigatório")
        String pushToken
) {
}
