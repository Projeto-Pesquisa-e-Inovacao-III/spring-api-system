package com.spring.ApiSystem.domain.agendamento.events;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.usuario.Usuario;

import java.util.List;

public class AgendamentoEventPublisher {

    private final List<AgendamentoListener> listeners;

    public AgendamentoEventPublisher(List<AgendamentoListener> listeners) {
        this.listeners = listeners;
    }

    public void publishAgendamentoCreatedEvent(Agendamento agendamento) {
        listeners.forEach(listener -> listener.onAgendamentoCreated(agendamento));
    }

    public void publishReagendamentoSolicitacaoEvent(Agendamento agendamento, Usuario usuario) {
        listeners.forEach(listeners -> listeners.onReagendamentoCreated(agendamento, usuario));
    }

    public void publishAgendamentoAprovadoEvent(Agendamento agendamento, Usuario usuario) {
        listeners.forEach(listeners -> listeners.onAprovacaoAgendamento(agendamento, usuario));

    }

    public void publishAgendamentoCanceladoEvent(Agendamento agendamento, Usuario usuario) {
        listeners.forEach(listeners -> listeners.onCancelamentoAgendamento(agendamento, usuario));
    }

    public void publishAgendamentoConcluidoEvent(Agendamento agendamento){
        listeners.forEach(listeners -> listeners.onConclusaoAgendamento(agendamento));
    }

    public void AusenciaRegistradaPersonalEvent(Agendamento agendamento){
        listeners.forEach(listeners -> listeners.onAusenciaRegistradaPersonal(agendamento));
    }

    public void AusenciaRegistradaAlunoEvent(Agendamento agendamento){
        listeners.forEach(listeners -> listeners.onAusenciaRegistradaAluno(agendamento));
    }

    public void AusenciaRegistradaAlunoJustificadoEvent(Agendamento agendamento){
        listeners.forEach(listeners -> listeners.onAusenciaRegistradaAlunoJustificado(agendamento));
    }


}
