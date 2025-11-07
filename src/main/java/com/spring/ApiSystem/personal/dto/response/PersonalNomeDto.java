package com.spring.ApiSystem.personal.dto.response;

import com.spring.ApiSystem.personal.Personal;

import java.io.Serializable;

/**
 * DTO for {@link Personal}
 */
public record PersonalNomeDto(String nome) implements Serializable {
}