package com.spring.ApiSystem.dto.ProdutoExibicao.response;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.spring.ApiSystem.model.ProdutoExibicao}
 */
public record ProdutoExibicaoDto(String titulo, String subtitulo, String descricao, Double preco, String periodo,
                                 String status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
                                 String tipoAula, Integer quantidadeAula, Integer duracaoMes) implements Serializable {
}