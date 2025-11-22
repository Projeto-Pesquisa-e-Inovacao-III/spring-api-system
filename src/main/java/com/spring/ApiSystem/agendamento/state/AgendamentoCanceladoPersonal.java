package com.spring.ApiSystem.agendamento.state;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.agendamento.exception.AgendamentoStateException;

public class AgendamentoCanceladoPersonal implements AgendamentoState {

    @Override
    public AgendamentoStatus getSituacao() {
        return AgendamentoStatus.CANCELADO_PERSONAL;
    }

    @Override
    public AgendamentoState aprovado() {
        throw new AgendamentoStateException("Agendamento cancelado.");
    }

    @Override
    public AgendamentoState pendenteClienteAprovacao() {
        throw new AgendamentoStateException("Agendamento cancelado.");
    }

    @Override
    public AgendamentoState pendentePersonalAprovacao() {
        throw new AgendamentoStateException("Agendamento cancelado.");
    }

    @Override
    public AgendamentoState concluido() {
        throw new AgendamentoStateException("Agendamento cancelado.");
    }

    @Override
    public AgendamentoState pendentePersonalConcluir() {
        throw new AgendamentoStateException("Agendamento cancelado.");
    }

    @Override
    public AgendamentoState canceladoPersonal() {
        return this;
    }

    @Override
    public AgendamentoState canceladoCliente() {
        throw new AgendamentoStateException("Agendamento cancelado.");
    }

    @Override
    public AgendamentoState ausenciaPersonal() {
        throw new AgendamentoStateException("Agendamento cancelado.");
    }

    @Override
    public AgendamentoState ausenciaCliente() {
        throw new AgendamentoStateException("Agendamento cancelado.");
    }
}
