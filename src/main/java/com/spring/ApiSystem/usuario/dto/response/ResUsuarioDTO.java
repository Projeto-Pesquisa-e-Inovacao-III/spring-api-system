package com.spring.ApiSystem.usuario.dto.response;

import java.time.LocalDate;

public record ResUsuarioDTO(
    Long id,
    String nome,
    String sexo,
    LocalDate dataNascimento,
    String email,
    boolean ativo
) {}
