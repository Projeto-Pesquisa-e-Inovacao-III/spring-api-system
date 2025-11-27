package com.spring.ApiSystem.produtoexibicao.dto.response;

import com.spring.ApiSystem.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.produtoexibicao.enums.TipoProduto;

import java.time.LocalDateTime;

public record ResListaProdutoExibicaoDto(
    Long id,
    String titulo,
    String subtitulo,
    String descricao,
    Double preco,
    String periodo,
    ProdutoExibicaoStatus status,
    TipoAula tipoAula,
    TipoProduto tipoProduto,
    String quantidadeAula,
    Integer duracaoMes,
    LocalDateTime dataCriacao
) {}
