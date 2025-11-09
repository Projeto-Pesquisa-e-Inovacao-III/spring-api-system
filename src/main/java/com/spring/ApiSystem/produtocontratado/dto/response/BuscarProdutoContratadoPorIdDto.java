package com.spring.ApiSystem.produtocontratado.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record BuscarProdutoContratadoPorIdDto(
        Long id,
        Boolean ativo,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dataCompra,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dataExpiracao,
        Integer saldoAula,
        Long alunoId,
        Long produtoExibicaoId
) {}


