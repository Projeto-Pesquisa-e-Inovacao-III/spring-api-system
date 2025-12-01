package com.spring.ApiSystem.personal.dto.response;

import java.time.LocalDate;

public record ResListarPersonaisDTO(
    Long id,
    String nome,
    LocalDate dataNascimento,
    String caminhoFoto
) { }
