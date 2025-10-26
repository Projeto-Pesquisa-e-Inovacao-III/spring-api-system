package com.spring.ApiSystem.dto.cep.response;

public record DadosCepDTO(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf
) {}

