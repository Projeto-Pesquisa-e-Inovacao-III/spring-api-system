package com.spring.ApiSystem.agendamento.state;

import com.spring.ApiSystem.agendamento.enums.Situacao;
import com.spring.ApiSystem.agendamento.exception.AgendamentoStateException;

public class AgendamentoRecusado implements AgendamentoState {

    @Override
    public Situacao getSituacao() { return Situacao.RECUSADO; }

    @Override
    public AgendamentoState recusado() {
        return this;
    }

    @Override
    public AgendamentoState aceitar() {
        throw new AgendamentoStateException("Não é possível aceitar um agendamento que foi RECUSADO.");
    }

    @Override
    public AgendamentoState concluido() {
        throw new AgendamentoStateException("Não é possível concluir um agendamento RECUSADO.");
    }

    @Override
    public AgendamentoState pendenteCliente() {
        throw new AgendamentoStateException("Não é possível pendente em um agendamento RECUSADO.");
    }

    @Override
    public AgendamentoState pendentePersonal() {
        throw new AgendamentoStateException("Não é possível pendente em um agendamento RECUSADO.");
    }

}
