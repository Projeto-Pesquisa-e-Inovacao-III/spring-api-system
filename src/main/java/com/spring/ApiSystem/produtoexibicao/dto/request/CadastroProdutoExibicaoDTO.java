package com.spring.ApiSystem.produtoexibicao.dto.request;

import jakarta.validation.constraints.*;

public record CadastroProdutoExibicaoDTO(
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

    @NotBlank(message = "Status é obrigatório")
    String status,

    @NotBlank(message = "Tipo de aula é obrigatório")
    String tipoAula,

    @NotNull(message = "Quantidade de aulas é obrigatória")
    @Positive(message = "Quantidade de aulas deve ser positiva")
    Integer quantidadeAula,

    @NotNull(message = "Duração em meses é obrigatória")
    @Positive(message = "Duração deve ser positiva")
    Integer duracaoMes
) {}
