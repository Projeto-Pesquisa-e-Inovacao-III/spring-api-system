package com.spring.ApiSystem.domain.personal.dto.response;

import com.spring.ApiSystem.domain.personal.Personal;

import java.io.Serializable;

/**
 * DTO for {@link Personal}
 * */

public record PersonalNomeDto(String nome) implements Serializable {
}