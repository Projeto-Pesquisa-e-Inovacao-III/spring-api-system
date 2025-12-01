package com.spring.ApiSystem.produtocontratado.dto.response;

import com.spring.ApiSystem.produtoexibicao.dto.response.ProdutoExibicaoDto;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link ProdutoContratado}
 */
public record ResProdutoContratadoDto(
        Long id,
        Boolean situacao,
        LocalDate dataCompra,
        LocalDate dataExpiracao,
        Integer saldoAula,
        @NotNull ProdutoExibicaoDto produtoExibicao
) implements Serializable {
}