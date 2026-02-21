package com.spring.ApiSystem.domain.produtoexibicao.dto.response;

import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicao;

import java.io.Serializable;

/**
 * DTO for {@link ProdutoExibicao}
 */
public record ProdutoExibicaoTituloETipoAulaDto(String titulo, String tipoAula, String tipoProduto) implements Serializable {
}