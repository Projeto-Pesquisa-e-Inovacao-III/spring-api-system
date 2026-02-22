package com.spring.ApiSystem.domain.personal.dto.response;

import com.spring.ApiSystem.domain.personal.Personal;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Personal}
 */
public record PersonalDto(
        String nome,
        String sexo,
        LocalDate dataNascimento,
        String email,
        String salt,
        String senha,
        boolean ativo,
        String cref
) implements Serializable {}