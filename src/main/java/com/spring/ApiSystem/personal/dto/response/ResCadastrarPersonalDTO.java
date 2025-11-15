package com.spring.ApiSystem.personal.dto.response;

import java.time.LocalDate;

public record ResCadastrarPersonalDTO(
    Long id,
    String nome,
    String sexo,
    LocalDate dataNascimento,
    String email,
    boolean ativo

) {}
