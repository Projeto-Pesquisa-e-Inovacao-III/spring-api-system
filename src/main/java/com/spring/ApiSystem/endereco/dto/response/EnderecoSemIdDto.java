package com.spring.ApiSystem.endereco.dto.response;

import com.spring.ApiSystem.cep.dto.response.CEPDto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EnderecoSemIdDto(String numero, String complemento, String unidade, String tipo,
                               LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
                               CEPDto cep) implements Serializable {
}