package com.spring.ApiSystem.domain.produtoexibicao.dto.response;



import com.spring.ApiSystem.domain.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;

import java.time.LocalDateTime;

public record ResProdutoExibicaoDto(
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
