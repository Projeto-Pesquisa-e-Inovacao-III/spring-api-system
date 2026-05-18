package com.spring.ApiSystem.domain.resumoAgendamento.dto.req;

import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReqCadastrarResumoAgendamentoDTO(
        @NotNull(message = "O id do aluno é obrigatório")
        Long idAluno,

        @NotNull(message = "O id do personal é obrigatório")
        Long idPersonal,

        @NotBlank(message = "O resumo é obrigatório")
        String resumo,

        @NotNull(message = "O grupo muscular é obrigatório")
        GrupoMuscular grupoMuscular
) {}
