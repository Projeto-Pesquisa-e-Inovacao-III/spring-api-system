package com.spring.ApiSystem.application.service;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EditarUsuarioDTO {
    private String nome;

    @Email(message = "O email deve ser válido")
    private String email;


}