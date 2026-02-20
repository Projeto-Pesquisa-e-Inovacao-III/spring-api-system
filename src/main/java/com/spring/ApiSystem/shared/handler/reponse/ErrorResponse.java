package com.spring.ApiSystem.handler.reponse;

import java.time.LocalDateTime;

public record ErrorResponse(
        Integer status,
        String mensagem,
        LocalDateTime timestamp
) {}
