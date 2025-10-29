package com.spring.ApiSystem.produtocontratado.dto.response;

import com.spring.ApiSystem.produtoexibicao.dto.response.ProdutoExibicaoTituloETipoAulaDto;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;

import java.io.Serializable;

/**
 * DTO for {@link ProdutoContratado}
 */
public record ProdutoContratadoSoComProdutoDto(ProdutoExibicaoTituloETipoAulaDto produtoExibicao) implements Serializable {
}