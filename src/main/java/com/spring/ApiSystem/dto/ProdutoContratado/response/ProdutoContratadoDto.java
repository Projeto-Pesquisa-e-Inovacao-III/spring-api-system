package com.spring.ApiSystem.dto.ProdutoContratado.response;

import com.spring.ApiSystem.dto.ProdutoExibicao.response.ProdutoExibicaoDto;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.spring.ApiSystem.model.ProdutoContratado}
 */
public record ProdutoContratadoDto(Boolean ativo, Date dataCompra, Date dataExpiracao, Integer saldoAula,
                                   @NotNull ProdutoExibicaoDto produtoExibicao) implements Serializable {
}