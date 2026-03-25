package com.spring.ApiSystem.domain.usuario.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReqAtualizarSenhaDto(
        @NotBlank(message = "Senha atual obrigatória")
        String senhaAtual,

        @NotBlank(message = "Nova senha obrigatória")
        String senhaNova
){}
