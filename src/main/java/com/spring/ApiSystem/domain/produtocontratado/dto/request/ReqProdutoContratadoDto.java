package com.spring.ApiSystem.domain.produtocontratado.dto.request;

import java.time.LocalDate;

public record ReqProdutoContratadoDto (
        String nomeProduto,
        LocalDate dataInicio,
        LocalDate dataFim
){}
