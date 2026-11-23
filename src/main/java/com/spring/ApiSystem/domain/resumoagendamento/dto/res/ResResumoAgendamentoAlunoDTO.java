package com.spring.ApiSystem.domain.resumoagendamento.dto.res;

import java.time.LocalDateTime;
import java.util.List;

public record ResResumoAgendamentoAlunoDTO(
        String nomeAluno,
        String nomePersonal,
        LocalDateTime agendamentoData,
        String resumo,
        List<String> grupoMuscular
){}
