package com.spring.ApiSystem.dto.ProdutoContratado.response;

import com.spring.ApiSystem.dto.ProdutoExibicao.response.ProdutoExibicaoTituloETipoAulaDto;
import com.spring.ApiSystem.model.ProdutoContratado;

import java.io.Serializable;

/**
 * DTO for {@link ProdutoContratado}
 */
public record ProdutoContratadoSoComProdutoDto(ProdutoExibicaoTituloETipoAulaDto produtoExibicao) implements Serializable {
}