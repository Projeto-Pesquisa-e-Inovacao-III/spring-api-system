package com.spring.ApiSystem.domain.produtocontratado.dto.response;

public record ResListarGanhoMensalDto(
        Integer ano,
        Integer mes,
        Double totalPreco
){}
