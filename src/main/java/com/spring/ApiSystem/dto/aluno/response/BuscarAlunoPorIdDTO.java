package com.spring.ApiSystem.dto.aluno.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record BuscarAlunoPorIdDTO(
        Long id,
        String nome,
        String sexo,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date dataNascimento,
        String email,
        String cpf,
        boolean ativo
) {}

