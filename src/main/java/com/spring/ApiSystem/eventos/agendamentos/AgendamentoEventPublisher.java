package com.spring.ApiSystem.eventos.agendamentos;

import com.spring.ApiSystem.agendamento.Agendamento;

import java.util.List;

public class AgendamentoEventPublisher {

    private final List<AgendamentoListener> listeners;

    public AgendamentoEventPublisher(List<AgendamentoListener> listeners) {
        this.listeners = listeners;
    }

    public void publishAgendamentoCreatedEvent(Agendamento agendamento) {
        listeners.forEach(listener -> listener.onAgendamentoCreated(agendamento));
    }
}
