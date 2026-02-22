package com.spring.ApiSystem.domain.cep.dto.response;

import com.spring.ApiSystem.domain.cep.CEP;

import java.io.Serializable;

/**
 * DTO for {@link CEP}
 */
public record CEPDto(
        String id,
        String logradouro,
        String bairro,
        String localidade,
        String uf
) implements Serializable {}