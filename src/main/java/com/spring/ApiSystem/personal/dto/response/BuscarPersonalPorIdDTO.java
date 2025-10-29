package com.spring.ApiSystem.personal.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record BuscarPersonalPorIdDTO(
        Long id,
        String nome,
        String sexo,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date dataNascimento,
        String email,
        String cref,
        boolean ativo
) {}

