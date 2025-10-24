package com.spring.ApiSystem.dto.usuario.request;

import jakarta.validation.constraints.*;

import java.util.Date;

public record EditarUsuarioDTO(
    @NotBlank(message = "O nome não pode ficar vazio ou nulo")
    String nome,

    String sexo,

    @NotBlank(message = "A data de nascimento não pode ficar vazia ou nula")
    @Past(message = "A data de nascimento deve estar no passado")
    Date dataNascimento,

    @NotBlank(message = "O email não pode ficar vazio ou nulo")
    @Email(message = "Email deve ter formato válido")
    String email,

    @NotBlank(message = "A senha atual não pode ficar vazia ou nula")
    @Size(min = 6, message = "A senha atual deve ter no mínimo 6 caracteres")
    String senha,

    @Size(min = 6, message = "A senha nova deve ter no mínimo 6 caracteres")
    String senhaNova
) {}