package com.spring.ApiSystem.domain.usuario.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReqAtualizarSenhaDto(
        @NotBlank(message = "Senha atual obrigatória")
        String senhaAtual,

        @NotBlank(message = "Nova senha obrigatória")
        @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres")
        String senhaNova
){}
