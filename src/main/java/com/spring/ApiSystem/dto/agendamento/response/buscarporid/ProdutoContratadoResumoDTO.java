package com.spring.ApiSystem.dto.agendamento.response.buscarporid;

import com.spring.ApiSystem.dto.agendamento.response.AulaDto;

public record ProdutoContratadoResumoDTO(
    Long id,
    AulaDto aulaDto
) {}

