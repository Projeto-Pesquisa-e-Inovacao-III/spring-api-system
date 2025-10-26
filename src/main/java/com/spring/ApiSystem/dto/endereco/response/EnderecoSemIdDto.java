package com.spring.ApiSystem.dto.endereco.response;

import com.spring.ApiSystem.dto.Cep.response.CEPDto;
import com.spring.ApiSystem.model.Endereco;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Endereco}
 */
public record EnderecoSemIdDto(String numero, String complemento, String unidade, String tipo,
                               LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
                               CEPDto cep) implements Serializable {
}