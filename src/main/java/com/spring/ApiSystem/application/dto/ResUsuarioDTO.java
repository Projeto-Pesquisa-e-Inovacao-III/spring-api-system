package com.spring.ApiSystem.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResUsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private boolean ativo;
}