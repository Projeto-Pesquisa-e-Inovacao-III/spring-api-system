package com.spring.ApiSystem.aluno.dto.response;

import com.spring.ApiSystem.aluno.Aluno;

import java.io.Serializable;

/**
 * DTO for {@link Aluno}
 */
public record AlunoNomeDto(String nome) implements Serializable {
}