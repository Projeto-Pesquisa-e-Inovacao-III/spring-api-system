package com.spring.ApiSystem.agendamento.state;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.agendamento.exception.AgendamentoStateException;

public class AgendamentoPendentePersonalAprovacao implements AgendamentoState {

    @Override
    public AgendamentoStatus getSituacao() {
        return AgendamentoStatus.PENDENTE_PERSONAL_APROVACAO;
    }

    @Override
    public AgendamentoState aprovado() {
        return new AgendamentoAprovado();
    }

    @Override
    public AgendamentoState pendenteClienteAprovacao() {
        return new AgendamentoPendenteClienteAprovacao();
    }

    @Override
    public AgendamentoState pendentePersonalAprovacao() {
        return this;
    }

    @Override
    public AgendamentoState concluido() {
        throw new AgendamentoStateException("Ainda não aprovado.");
    }

    @Override
    public AgendamentoState pendentePersonalConcluir() {
        throw new AgendamentoStateException("Ainda não aprovado.");
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
        throw new AgendamentoStateException("Pendente aprovação do personal.");
    }

    @Override
    public AgendamentoState ausenciaCliente() {
        throw new AgendamentoStateException("Pendente aprovação do personal.");
    }
}
