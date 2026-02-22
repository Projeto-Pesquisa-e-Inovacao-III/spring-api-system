package com.spring.ApiSystem.domain.usuario.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ReqEditarUsuarioDTO(
    @NotBlank(message = "O nome não pode ficar vazio ou nulo")
    String nome,

    @NotBlank(message = "O sexo não pode ficar vazio ou nulo")
    String sexo,

    @Past(message = "A data de nascimento deve estar no passado")
    LocalDate dataNascimento,

    @NotBlank(message = "O email não pode ficar vazio ou nulo")
    @Email(message = "Email deve ter formato válido")
    String email,

    @NotBlank(message = "A senha atual não pode ficar vazia ou nula")
    @Size(min = 6, message = "A senha atual deve ter no mínimo 6 caracteres")
    String senha,

    @Size(min = 6, message = "A senha nova deve ter no mínimo 6 caracteres")
    String senhaNova

) {}