package com.spring.ApiSystem.agendamento.dto.response;

import java.time.LocalDateTime;

public record ResBuscarAgendamentosParaCalendarioPorAluno(
    Long agendamentoId,
    LocalDateTime data
){ }
