package com.spring.ApiSystem.domain.resumoAgendamento.dto.res;

import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ResResumoAgendamentoAlunoDTO(
        String nomeAluno,
        String nomePersonal,
        LocalDateTime agendamentoData,
        String resumo,
        List<GrupoMuscular> grupoMuscular
){}
