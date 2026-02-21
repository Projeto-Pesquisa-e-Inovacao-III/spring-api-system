package com.spring.ApiSystem.domain.produtoexibicao.dto.request;


import com.spring.ApiSystem.domain.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReqEdicaoProdutoExibicaoDTO(
    @NotBlank(message = "Título é obrigatório")
    String titulo,

    String subtitulo,

    @NotBlank(message = "Descrição é obrigatória")
    String descricao,

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

    public ReqEdicaoProdutoExibicaoDTO withTipoProduto(TipoProduto newTipoProduto){
        return new ReqEdicaoProdutoExibicaoDTO(
                this.titulo,
                this.subtitulo,
                this.descricao,
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
