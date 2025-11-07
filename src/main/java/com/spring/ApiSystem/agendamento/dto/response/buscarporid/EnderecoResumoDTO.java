package com.spring.ApiSystem.agendamento.dto.response.buscarporid;

public record EnderecoResumoDTO(
    Long id,
    String numero,
    String complemento,
    String unidade,
    String tipo,
    CepResumoDTO cep
) {}

