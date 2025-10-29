package com.spring.ApiSystem.produtoexibicao.dto.response;

import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link ProdutoExibicao}
 */
public record ProdutoExibicaoDto(String titulo, String subtitulo, String descricao, Double preco, String periodo,
                                 String status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
                                 String tipoAula, Integer quantidadeAula, Integer duracaoMes) implements Serializable {
}