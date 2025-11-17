package com.spring.ApiSystem.produtoexibicao.dto.response;

import com.spring.ApiSystem.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;

import java.time.LocalDateTime;

public record ResProdutoExibicaoDTO(
    String titulo,
    String subtitulo,
    String descricao,
    Double preco,
    String periodo,
    ProdutoExibicaoStatus status,
    TipoAula tipoAula,
    String quantidadeAula,
    Integer duracaoMes,
    LocalDateTime dataCriacao
) {}
