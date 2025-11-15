package com.spring.ApiSystem.aluno.dto.response;

import java.time.LocalDate;

public record ResCadastrarAlunoDTO(
    Long id,
    String nome,
    String sexo,
    LocalDate dataNascimento,
    String email,
    boolean ativo
) {}
