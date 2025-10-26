package com.spring.ApiSystem.dto.produtoExibicao.response;

import com.spring.ApiSystem.enums.Status;

import java.time.LocalDateTime;

public record ResProdutoExibicaoDTO(
    String titulo,
    String subtitulo,
    String descricao,
    Double preco,
    String periodo,
    Status status,
    String tipoAula,
    Integer quantidadeAula,
    Integer duracaoMes,
    LocalDateTime dataCriacao,
    LocalDateTime dataAtualizacao
) {}
