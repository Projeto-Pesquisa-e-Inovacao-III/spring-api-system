package com.spring.ApiSystem.agendamento.state;

import com.spring.ApiSystem.agendamento.exception.AgendamentoStateException;
import com.spring.ApiSystem.agendamento.enums.Situacao;

public class AgendamentoPendenteCliente implements AgendamentoState {

    @Override
    public Situacao getSituacao() { return Situacao.PENDENTE_CLIENTE; }

    @Override
    public AgendamentoState recusado() {
        return new AgendamentoRecusado();
    }

    @Override
    public AgendamentoState aceitar() {
        return new AgendamentoAceito();
    }



    @Override
    public AgendamentoState pendenteCliente() {
        return this;
    }

    @Override
    public AgendamentoState pendentePersonal() {
        return new AgendamentoPendentePersonal();
    }
    @Override
    public AgendamentoState concluido() {
        throw new AgendamentoStateException("Não é possível concluir um agendamento PENDENTE diretamente.");
    }

}
