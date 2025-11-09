package com.spring.ApiSystem.produtocontratado.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record BuscarProdutoContratadoPorIdDto(
        Long id,
        Boolean situacao,
        LocalDate dataCompra,
        LocalDate dataExpiracao,
        Integer saldoAula,
        Long alunoId,
        Long produtoExibicaoId
) {}


