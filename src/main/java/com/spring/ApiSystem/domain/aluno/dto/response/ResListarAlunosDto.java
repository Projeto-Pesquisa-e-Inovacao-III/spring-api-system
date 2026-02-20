package com.spring.ApiSystem.aluno.dto.response;

public record ResListarAlunosDto(
    Long id,
    String nome,
    Integer idade,
    String caminhoFoto
) {}
