package com.spring.ApiSystem.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditarUsuarioDTO {
    private String nome;

    // Update das validações da classe - 01/09/25 - Vitor Suave
    @Email(message = "Email deve ter formato válido")
    private String email;

    @NotBlank(message = "A senha deve ser válida")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotNull(message = "O CPF é obrigatório")
    @Size(min = 11, message = "O CPF deve conter 11 dígitos")
    private String cpf;

}