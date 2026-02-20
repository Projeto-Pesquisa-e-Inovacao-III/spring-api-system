package com.spring.ApiSystem.domain.produtocontratado.dto.response;

import java.time.LocalDate;

public record ResProdutoContratadoAtivoDto(
        String nome,
        LocalDate dataExpiracao
){}
