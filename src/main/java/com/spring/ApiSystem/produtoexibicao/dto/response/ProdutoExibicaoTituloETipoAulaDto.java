package com.spring.ApiSystem.produtoexibicao.dto.response;

import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.produtoexibicao.enums.TipoProduto;

import java.io.Serializable;

/**
 * DTO for {@link ProdutoExibicao}
 */
public record ProdutoExibicaoTituloETipoAulaDto(String titulo, String tipoAula, String tipoProduto) implements Serializable {
}