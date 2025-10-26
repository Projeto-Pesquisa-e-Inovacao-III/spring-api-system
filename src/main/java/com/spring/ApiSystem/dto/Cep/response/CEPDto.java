package com.spring.ApiSystem.dto.cep.response;

import java.io.Serializable;

/**
 * DTO for {@link com.spring.ApiSystem.model.CEP}
 */
public record CEPDto(String id, String logradouro, String bairro, String localidade,
                     String uf) implements Serializable {
}