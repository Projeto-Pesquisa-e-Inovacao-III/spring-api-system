package com.spring.ApiSystem.domain.produtoexibicao.dto.request;


import com.spring.ApiSystem.domain.beneficio.Beneficio;
import com.spring.ApiSystem.domain.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record ReqEdicaoProdutoExibicaoDto(
    @NotBlank(message = "Título é obrigatório")
    String titulo,

    String subtitulo,

    String descricao,

    @Valid
    @NotNull(message = "Benefícios são obrigatórios")
    @Size(min = 1, max = 8,  message = "Deve haver entre 1 e 8 benefícios")
    List<Beneficio> beneficios,

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    @DecimalMin(value = "0.01", message = "Preço mínimo é 0.01")
    Double preco,

    @NotBlank(message = "Período é obrigatório")
    String periodo,

    @NotNull(message = "Status é obrigatório")
    ProdutoExibicaoStatus status,

    @NotNull(message = "Tipo de aula é obrigatório")
    TipoAula tipoAula,

    TipoProduto tipoProduto,

    @NotNull(message = "Quantidade de aulas é obrigatória")
    String quantidadeAula,

    @NotNull(message = "Duração em meses é obrigatória")
    @Positive(message = "Duração deve ser positiva")
    Integer duracaoMes
) {

    public ReqEdicaoProdutoExibicaoDto withTipoProduto(TipoProduto newTipoProduto){
        return new ReqEdicaoProdutoExibicaoDto(
                this.titulo,
                this.subtitulo,
                this.descricao,
                this.beneficios,
                this.preco,
                this.periodo,
                this.status,
                this.tipoAula,
                newTipoProduto,
                this.quantidadeAula,
                this.duracaoMes
        );
    }
}
