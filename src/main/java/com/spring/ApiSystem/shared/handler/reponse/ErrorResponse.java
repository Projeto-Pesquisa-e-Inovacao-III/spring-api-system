package com.spring.ApiSystem.shared.handler.reponse;

import java.time.LocalDateTime;

public record ErrorResponse(
        Integer status,
        String mensagem,
        LocalDateTime timestamp
) {}
