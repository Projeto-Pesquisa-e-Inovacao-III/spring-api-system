package com.spring.ApiSystem.model.state;

import com.spring.ApiSystem.model.enums.Situacao;
import com.spring.ApiSystem.exception.AgendamentoStateException;

public class AgendamentoReagendado implements AgendamentoState {

    @Override
    public Situacao getSituacao() { return Situacao.REAGENDADO; }

    @Override
    public AgendamentoState recusado() {
        return new AgendamentoRecusado();
    }

    @Override
    public AgendamentoState aceitar() {
        return new AgendamentoAceito();
    }

    @Override
    public AgendamentoState concluido() {
        throw new AgendamentoStateException("Não é possível concluir um agendamento PENDENTE diretamente.");
    }

    @Override
    public AgendamentoState pendenteCliente() {
        return new AgendamentoPendenteCliente();
    }

    @Override
    public AgendamentoState pendentePersonal() {
        return new AgendamentoPendentePersonal();
    }

    @Override
    public AgendamentoState reagendar() {
        return this;
    }
}
