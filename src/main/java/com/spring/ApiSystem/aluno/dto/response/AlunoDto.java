package com.spring.ApiSystem.aluno.dto.response;

import com.spring.ApiSystem.aluno.Aluno;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link Aluno}
 */
public record AlunoDto(String nome, String sexo, Date dataNascimento, String email, String salt, String senha,
                       boolean ativo, String cpf) implements Serializable {
}