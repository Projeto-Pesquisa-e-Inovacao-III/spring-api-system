package com.spring.ApiSystem.domain.resumoAgendamento.dto.res;

import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;

public record ResCadastrarResumoAgendamentoDTO(
        Long idAluno,
        Long idPersonal,
        String resumo,
        GrupoMuscular grupoMuscular
) {}
