package com.spring.ApiSystem.domain.aluno.dto.response;



import com.spring.ApiSystem.domain.aluno.Aluno;

import java.io.Serializable;

/**
 * DTO for {@link Aluno}
 */
public record AlunoNomeDto(String nome) implements Serializable {
}