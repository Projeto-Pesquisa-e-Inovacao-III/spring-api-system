package com.spring.ApiSystem.produtocontratado.dto.response;

import java.time.LocalDate;

public record ResProdutoContratadoAtivoDto(
        String nome,
        LocalDate dataExpiracao
){}
