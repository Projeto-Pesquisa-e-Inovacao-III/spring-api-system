package com.spring.ApiSystem.domain.agendamento.projection;

public interface ResTotalAgendamentoByStatusProjection {
    Integer getTotalPendente();
    Integer getTotalRespondido();
    Integer getTotalCanceladoPorMesAtual();
    Integer getTotalAgendamentosHoje();
}