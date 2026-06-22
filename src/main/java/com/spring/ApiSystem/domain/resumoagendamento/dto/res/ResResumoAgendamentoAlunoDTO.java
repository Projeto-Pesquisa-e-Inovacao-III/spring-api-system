package com.spring.ApiSystem.domain.resumoagendamento.dto.res;

import com.spring.ApiSystem.domain.resumoagendamento.enums.GrupoMuscular;

import java.time.LocalDateTime;
import java.util.List;

public record ResResumoAgendamentoAlunoDTO(
        String nomeAluno,
        String nomePersonal,
        LocalDateTime agendamentoData,
        String resumo,
        List<GrupoMuscular> grupoMuscular
){}
