package com.spring.ApiSystem.domain.produtocontratado.dto.response;


import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratado;
import com.spring.ApiSystem.domain.produtoexibicao.dto.response.ProdutoExibicaoDto;
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