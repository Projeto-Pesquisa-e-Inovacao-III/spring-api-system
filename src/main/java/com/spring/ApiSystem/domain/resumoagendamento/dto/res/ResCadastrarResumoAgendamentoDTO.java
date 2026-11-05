package com.spring.ApiSystem.domain.resumoagendamento.dto.res;

import com.spring.ApiSystem.domain.resumoagendamento.enums.GrupoMuscular;

import java.util.List;

public record ResCadastrarResumoAgendamentoDTO(
        Long idAluno,
        Long idPersonal,
        String resumo,
        List<GrupoMuscular> grupoMuscular
) {}
