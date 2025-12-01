package com.spring.ApiSystem.produtocontratado.dto.response;

public record ResListarGanhoMensalDto(
        Integer mes,
        Integer ano,
        Double totalPreco
){}
