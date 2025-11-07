package com.spring.ApiSystem.agendamento.dto.response.buscarporid;

import com.spring.ApiSystem.agendamento.dto.response.AulaDto;

public record ProdutoContratadoResumoDTO(
    Long id,
    AulaDto aulaDto
) {}

