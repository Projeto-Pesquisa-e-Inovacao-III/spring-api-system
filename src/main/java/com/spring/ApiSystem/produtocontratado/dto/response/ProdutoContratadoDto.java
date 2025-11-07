package com.spring.ApiSystem.produtocontratado.dto.response;

import com.spring.ApiSystem.produtoexibicao.dto.response.ProdutoExibicaoDto;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link ProdutoContratado}
 */
public record ProdutoContratadoDto(Boolean ativo, Date dataCompra, Date dataExpiracao, Integer saldoAula,
                                   @NotNull ProdutoExibicaoDto produtoExibicao) implements Serializable {
}