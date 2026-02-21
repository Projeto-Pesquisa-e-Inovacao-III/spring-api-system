
package com.spring.ApiSystem.domain.agendamento.state;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.agendamento.exception.AgendamentoStateException;

public class AgendamentoPendenteClienteAprovacao implements AgendamentoState {

    @Override
    public AgendamentoStatus getSituacao() {
        return AgendamentoStatus.PENDENTE_CLIENTE_APROVACAO;
    }

    @Override
    public AgendamentoState aprovado() {
        return new AgendamentoAprovado();
    }

    @Override
    public AgendamentoState pendenteClienteAprovacao() {
        return this;
    }

    @Override
    public AgendamentoState pendentePersonalAprovacao() {
        return new AgendamentoPendentePersonalAprovacao();
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
        throw new AgendamentoStateException("Ainda não aprovado.");
    }

    @Override
    public AgendamentoState ausenciaCliente() {
        throw new AgendamentoStateException("Ainda não aprovado.");
    }
}
