package com.spring.ApiSystem.domain.produtoexibicao.dto.response;


import com.spring.ApiSystem.domain.beneficio.dto.response.ResListBeneficioDTO;
import com.spring.ApiSystem.domain.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;

import java.time.LocalDateTime;
import java.util.List;

public record ResListaProdutoExibicaoDto(
    Long id,
    String titulo,
    String subtitulo,
    String descricao,
    List<ResListBeneficioDTO> beneficios,
    Double preco,
    String periodo,
    ProdutoExibicaoStatus status,
    TipoAula tipoAula,
    TipoProduto tipoProduto,
    String quantidadeAula,
    Integer duracaoMes,
    LocalDateTime dataCriacao
) {}
