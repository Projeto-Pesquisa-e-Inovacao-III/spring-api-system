package com.spring.ApiSystem.domain.produtoexibicao.dto.request;


import com.spring.ApiSystem.domain.beneficio.Beneficio;
import com.spring.ApiSystem.domain.beneficio.dto.request.ReqBeneficioDTO;
import com.spring.ApiSystem.domain.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record ReqCadastroProdutoExibicaoDTO(
    @NotBlank(message = "Título é obrigatório")
    String titulo,

    String subtitulo,

    @NotBlank(message = "Descrição é obrigatória")
    String descricao,

    @Valid
    @NotNull(message = "Lista de benefícios não pode ser nula")
    @Size(min = 1, max = 8,  message = "Deve haver entre 1 e 8 benefícios")
    List<ReqBeneficioDTO> beneficios,

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

    @NotNull(message = "Tipo de produto é obrigatório")
    TipoProduto tipoProduto,

    @NotNull(message = "Quantidade de aulas é obrigatória")
    String quantidadeAula,

    @NotNull(message = "Duração em meses é obrigatória")
    @Positive(message = "Duração deve ser positiva")
    Integer duracaoMes
) {}
