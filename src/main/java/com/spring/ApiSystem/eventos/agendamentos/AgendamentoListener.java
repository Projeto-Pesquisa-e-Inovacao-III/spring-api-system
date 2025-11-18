package com.spring.ApiSystem.eventos.agendamentos;

import com.spring.ApiSystem.agendamento.Agendamento;

public interface AgendamentoListener {
    void onAgendamentoCreated(Agendamento agendamento);
}
