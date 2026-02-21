// java
package com.spring.ApiSystem.domain.agendamento.state;


import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.agendamento.exception.AgendamentoStateException;

public class AgendamentoPendentePersonalConcluir implements AgendamentoState {

    @Override
    public AgendamentoStatus getSituacao() {
        return AgendamentoStatus.PENDENTE_PERSONAL_CONCLUIR;
    }

    @Override
    public AgendamentoState aprovado() {
        throw new AgendamentoStateException("Agendamento já aprovado por ambos.");
    }

    @Override
    public AgendamentoState pendenteClienteAprovacao() {
        throw new AgendamentoStateException("Agendamento já aprovado pelo cliente.");
    }

    @Override
    public AgendamentoState pendentePersonalAprovacao() {
        throw new AgendamentoStateException("Agendamento já aprovado pelo personal.");
    }

    @Override
    public AgendamentoState concluido() {
        return new AgendamentoConcluido();
    }

    @Override
    public AgendamentoState pendentePersonalConcluir() {
        return this;
    }

    @Override
    public AgendamentoState canceladoPersonal() {
        throw new AgendamentoStateException("Agendamento não pode ser cancelado neste estado.");
    }

    @Override
    public AgendamentoState canceladoCliente() {
        throw new AgendamentoStateException("Agendamento não pode ser cancelado neste estado.");
    }

    @Override
    public AgendamentoState ausenciaPersonal() {
        return  new AgendamentoAusenciaPersonal();
    }

    @Override
    public AgendamentoState ausenciaCliente() {
        return  new AgendamentoAusenciaCliente();
    }
}
