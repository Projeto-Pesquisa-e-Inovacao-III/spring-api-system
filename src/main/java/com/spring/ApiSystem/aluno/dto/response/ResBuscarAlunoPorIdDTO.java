package com.spring.ApiSystem.aluno.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ResBuscarAlunoPorIdDTO(
        Long id,
        String nome,
        String sexo,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dataNascimento,
        String email,
        String cpf,
        boolean ativo,
        String caminhoFoto
) {}

