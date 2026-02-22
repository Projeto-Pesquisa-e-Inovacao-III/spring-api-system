package com.spring.ApiSystem.domain.usuario.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReqLoginUsuarioDTO(
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    String email,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha
) {}
