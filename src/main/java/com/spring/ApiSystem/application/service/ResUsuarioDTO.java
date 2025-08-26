package com.spring.ApiSystem.application.service;

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
    private Long cpf;
    private boolean ativo;
}