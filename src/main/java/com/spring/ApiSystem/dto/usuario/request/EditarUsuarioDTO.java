package com.spring.ApiSystem.dto.usuario.request;

import jakarta.validation.constraints.*;

import java.util.Date;

public record EditarUsuarioDTO(
    @NotBlank(message = "O nome não pode ficar vazio ou nulo")
    String nome,

    String sexo,

    @Past(message = "A data de nascimento deve estar no passado")
    Date dataNascimento,

    @Email(message = "Email deve ter formato válido")
    String email,

    @NotBlank(message = "A senha deve ser válida")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    String senha,

    @NotBlank(message = "A senha nova deve ser válida")
    @Size(min = 6, message = "A senha nova deve ter no mínimo 6 caracteres")
    String senhaNova
) {}