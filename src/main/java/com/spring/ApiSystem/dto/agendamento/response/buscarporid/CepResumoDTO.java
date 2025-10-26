package com.spring.ApiSystem.dto.agendamento.response.buscarporid;

public record CepResumoDTO(
    String id,
    String logradouro,
    String bairro,
    String localidade,
    String uf
) {}

