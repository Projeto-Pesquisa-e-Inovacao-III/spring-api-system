package com.spring.ApiSystem.domain.agendamento.state;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.agendamento.exception.AgendamentoStateException;



public class AgendamentoAprovado implements AgendamentoState {

    @Override
    public AgendamentoStatus getSituacao() {
        return AgendamentoStatus.APROVADO;
    }

    @Override
    public AgendamentoState aprovado() {
        return this;
    }

    @Override
    public AgendamentoState pendenteClienteAprovacao() {
        return new AgendamentoPendenteClienteAprovacao();
    }

    @Override
    public AgendamentoState pendentePersonalAprovacao() {
        return new AgendamentoPendentePersonalAprovacao();
    }

    @Override
    public AgendamentoState concluido() {
        throw new AgendamentoStateException("A conclusão deve ser feita  pelo personal.");
    }

    @Override
    public AgendamentoState pendentePersonalConcluir() {
        return new AgendamentoPendentePersonalConcluir();
    }

    @Override
    public AgendamentoState canceladoPersonal() {
        return new AgendamentoCanceladoPersonal();
    }

    @Override
    public AgendamentoState canceladoCliente() {
        return new AgendamentoCanceladoCliente();
    }

    @Override
    public AgendamentoState ausenciaPersonal() {
        throw new AgendamentoStateException("A conclusão deve antes de registrar uma ausencia.");
    }

    @Override
    public AgendamentoState ausenciaCliente() {
        throw new AgendamentoStateException("A conclusão deve antes de registrar uma ausencia.");
    }
}
