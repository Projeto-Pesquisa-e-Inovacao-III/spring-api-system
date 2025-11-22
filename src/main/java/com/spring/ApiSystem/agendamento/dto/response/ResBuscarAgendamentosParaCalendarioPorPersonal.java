package com.spring.ApiSystem.agendamento.dto.response;

import java.time.LocalDateTime;

public record ResBuscarAgendamentosParaCalendarioPorPersonal(
    Long agendamentoId,
    LocalDateTime data
){ }
