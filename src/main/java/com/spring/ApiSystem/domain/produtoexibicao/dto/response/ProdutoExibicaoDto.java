package com.spring.ApiSystem.domain.produtoexibicao.dto.response;

import com.spring.ApiSystem.domain.produtoexibicao.ProdutoExibicao;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;


import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link ProdutoExibicao}
 */
public record ProdutoExibicaoDto(String titulo, String subtitulo, String descricao, Double preco, String periodo,
                                 String status, LocalDateTime dataCriacao,
                                 TipoAula tipoAula, Integer quantidadeAula, Integer duracaoMes,
                                 TipoProduto tipoProduto) implements Serializable {
}