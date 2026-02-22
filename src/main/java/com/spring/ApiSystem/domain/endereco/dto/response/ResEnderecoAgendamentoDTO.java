package com.spring.ApiSystem.domain.endereco.dto.response;

public record ResEnderecoAgendamentoDTO(
        String numero,
        String bairro,
        String cidade,
        String uf
) {}
