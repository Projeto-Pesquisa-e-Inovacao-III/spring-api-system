package com.spring.ApiSystem.agendamento.state;

import com.spring.ApiSystem.agendamento.enums.Situacao;

public class AgendamentoAceito implements AgendamentoState {

    @Override
    public Situacao getSituacao() { return Situacao.ACEITO; }

    @Override
    public AgendamentoState recusado() {
        return new AgendamentoRecusado();
    }

    @Override
    public AgendamentoState aceitar() {
        return this;
    }

    @Override
    public AgendamentoState concluido() {
        return new AgendamentoConcluido();
    }

    @Override
    public AgendamentoState pendenteCliente() {
        return new AgendamentoPendenteCliente();
    }

    @Override
    public AgendamentoState pendentePersonal() {
        return new AgendamentoPendentePersonal();
    }

}
