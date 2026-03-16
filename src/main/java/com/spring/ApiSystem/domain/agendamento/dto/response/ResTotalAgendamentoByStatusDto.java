package com.spring.ApiSystem.domain.agendamento.dto.response;

public record ResTotalAgendamentoByStatusDto(
    Integer totalPendente,
    Integer totalRespondido,
    Integer totalCanceladoPorMesAtual,
    Integer totalAgendamentosHoje
){}