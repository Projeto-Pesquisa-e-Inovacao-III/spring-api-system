package com.spring.ApiSystem.agendamento.dto.response.buscarporid;


import com.spring.ApiSystem.agendamento.dto.response.ResProdutoContratadoTipoDeAulaComId;

public record ProdutoContratadoResumoDTO(
        Long id,
        ResProdutoContratadoTipoDeAulaComId aulaDto
) {}

