package com.spring.ApiSystem.domain.cep.dto.response;

public record DadosCepDTO(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf
) {}

