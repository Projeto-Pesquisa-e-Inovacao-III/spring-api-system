package com.spring.ApiSystem.agendamento.dto.response.buscarporid;

public record CepResumoDTO(
    String id,
    String logradouro,
    String bairro,
    String localidade,
    String uf
) {}

