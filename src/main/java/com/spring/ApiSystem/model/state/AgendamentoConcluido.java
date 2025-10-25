package com.spring.ApiSystem.model.state;

import com.spring.ApiSystem.model.enums.Situacao;
import com.spring.ApiSystem.exception.AgendamentoStateException;

public class AgendamentoConcluido implements AgendamentoState {

    @Override
    public Situacao getSituacao() { return Situacao.CONCLUIDO; }

    @Override
    public AgendamentoState recusado() {
        throw new AgendamentoStateException("Não é possível ir para RECUSADO em um agendamento CONCLUÍDO.");
    }

    @Override
    public AgendamentoState aceitar() {
        throw new AgendamentoStateException("Não é possível aceitar um agendamento já CONCLUÍDO.");
    }

    @Override
    public AgendamentoState concluido() {
        return this;
    }

    @Override
    public AgendamentoState pendenteCliente() {
        throw new AgendamentoStateException("Não é possível ter um agendamento PENDENTE.");
    }

    @Override
    public AgendamentoState pendentePersonal() {
        throw new AgendamentoStateException("Não é possível ter um agendamento PENDENTE.");
    }

    @Override
    public AgendamentoState reagendar() {
        throw new AgendamentoStateException("Não é possível reagendar um agendamento CONCLUÍDO.");
    }
}
