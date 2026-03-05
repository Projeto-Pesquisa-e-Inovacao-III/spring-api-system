package com.spring.ApiSystem.domain.aluno.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReqCpfDTO(
        @NotBlank(message = "O cpf é obrigatório")
        String value
) {}
