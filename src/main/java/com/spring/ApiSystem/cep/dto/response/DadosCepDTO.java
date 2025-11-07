package com.spring.ApiSystem.cep.dto.response;

public record DadosCepDTO(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf
) {}

