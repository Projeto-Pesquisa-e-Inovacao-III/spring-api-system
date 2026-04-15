package com.spring.ApiSystem.domain.personal.dto.response;

import java.time.LocalDate;

public record ResCadastrarPersonalDTO(
    Long id,
    String nome,
    String sexo,
    LocalDate dataNascimento,
    String email,
    boolean ativo,
    Integer bufferMinutos

) {}
