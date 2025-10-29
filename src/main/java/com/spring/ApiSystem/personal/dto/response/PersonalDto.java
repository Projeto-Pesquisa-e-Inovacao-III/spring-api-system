package com.spring.ApiSystem.personal.dto.response;

import com.spring.ApiSystem.personal.Personal;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link Personal}
 */
public record PersonalDto(String nome, String sexo, Date dataNascimento, String email, String salt, String senha,
                          boolean ativo, String cref) implements Serializable {
}