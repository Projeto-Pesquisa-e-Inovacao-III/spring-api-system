package com.spring.ApiSystem.dto.usuario.response;

import java.io.Serializable;

/**
 * DTO for {@link com.spring.ApiSystem.model.Aluno}
 */
public record AlunoNomeDto(String nome) implements Serializable {
}