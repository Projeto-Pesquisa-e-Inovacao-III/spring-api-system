package com.spring.ApiSystem.domain.resumoAgendamento.dto.res;

import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;

import java.util.List;

public record ResResumoAgendamentoAlunoDTO(
        String nomeAluno,
        String resumo,
        List<GrupoMuscular> grupoMuscular
){}
