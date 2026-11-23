package com.spring.ApiSystem.domain.resumoagendamento.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReqCadastrarResumoAgendamentoDTO(
        @NotBlank(message = "O resumo é obrigatório")
        String resumo,

        @NotNull(message = "O(s) id(s) do(s) musculo(s) trabalhado(s) é obrigatório")
        List<Long> grupoMuscular
) {}
