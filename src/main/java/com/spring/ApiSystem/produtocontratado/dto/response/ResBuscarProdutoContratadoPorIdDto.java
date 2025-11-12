package com.spring.ApiSystem.produtocontratado.dto.response;

import java.time.LocalDate;

public record ResBuscarProdutoContratadoPorIdDto(
        Long id,
        Boolean situacao,
        LocalDate dataCompra,
        LocalDate dataExpiracao,
        Integer saldoAula,
        Long alunoId,
        Long produtoExibicaoId
) {}


