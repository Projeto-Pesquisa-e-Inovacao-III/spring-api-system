package com.spring.ApiSystem.dto.usuario.response;

import com.spring.ApiSystem.model.Personal;

import java.io.Serializable;

/**
 * DTO for {@link Personal}
 */
public record PersonalNomeDto(String nome) implements Serializable {
}