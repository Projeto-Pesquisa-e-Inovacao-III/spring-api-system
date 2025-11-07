package com.spring.ApiSystem.produtocontratado.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record BuscarProdutoContratadoPorId(
        Long id,
        Boolean ativo,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date dataCompra,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date dataExpiracao,
        Integer saldoAula,
        Long alunoId,
        Long produtoExibicaoId
) {}


