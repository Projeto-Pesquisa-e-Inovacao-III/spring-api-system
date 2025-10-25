package com.spring.ApiSystem.dto.usuario.response;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.spring.ApiSystem.model.Aluno}
 */
public record AlunoDto(String nome, String sexo, Date dataNascimento, String email, String salt, String senha,
                       boolean ativo, String cpf) implements Serializable {
}