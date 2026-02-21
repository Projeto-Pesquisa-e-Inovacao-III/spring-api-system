package com.spring.ApiSystem.domain.agendamento.state;


import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.agendamento.exception.AgendamentoStateException;

public class AgendamentoCanceladoCliente implements AgendamentoState {

    @Override
    public AgendamentoStatus getSituacao() {
        return AgendamentoStatus.CANCELADO_CLIENTE;
    }

    @Override
    public AgendamentoState aprovado() {
        throw new AgendamentoStateException("Agendamento cancelado pelo cliente.");
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
        throw new AgendamentoStateException("Agendamento cancelado.");
    }

    @Override
    public AgendamentoState canceladoCliente() {
        return this;
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
