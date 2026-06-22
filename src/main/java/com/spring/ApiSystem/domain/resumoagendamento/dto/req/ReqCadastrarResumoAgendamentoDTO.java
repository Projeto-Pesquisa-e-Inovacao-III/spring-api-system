package com.spring.ApiSystem.domain.resumoagendamento.dto.req;

import com.spring.ApiSystem.domain.resumoagendamento.enums.GrupoMuscular;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReqCadastrarResumoAgendamentoDTO(
        @NotBlank(message = "O resumo é obrigatório")
        String resumo,

        @NotNull(message = "O grupo muscular é obrigatório")
        List<GrupoMuscular> grupoMuscular
) {}
