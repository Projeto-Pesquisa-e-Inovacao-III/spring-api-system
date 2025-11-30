package com.spring.ApiSystem.produtocontratado.dto.response;

import java.time.LocalDate;

public record ResProdutoContratadoDetalhadoDTO(
        Long id,
        String nomeComprador,
        String emailComprador,
        String telefone,
        String cpf,
        String produtoComprado,
        Double valorCompra,
        LocalDate dataCompra
) {}
