package com.spring.ApiSystem.produtoexibicao.dto.response;

import com.spring.ApiSystem.produtoexibicao.enums.Status;

import java.time.LocalDateTime;

public record ResListaProdutoExibicaoDTO(
    Long id,
    String titulo,
    String subtitulo,
    String descricao,
    Double preco,
    String periodo,
    Status status,
    String tipoAula,
    String quantidadeAula,
    Integer duracaoMes,
    LocalDateTime dataCriacao
) {}
