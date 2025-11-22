// java
package com.spring.ApiSystem.agendamento.state;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.agendamento.exception.AgendamentoStateException;

public class AgendamentoAusenciaPersonal implements AgendamentoState {

    @Override
    public AgendamentoStatus getSituacao() {
        return AgendamentoStatus.AUSENCIA_PERSONAL;
    }

    @Override
    public AgendamentoState aprovado() {
        throw new AgendamentoStateException("Personal ausente.");
    }

    @Override
    public AgendamentoState pendenteClienteAprovacao() {
        throw new AgendamentoStateException("Personal ausente.");
    }

    @Override
    public AgendamentoState pendentePersonalAprovacao() {
        throw new AgendamentoStateException("Personal ausente.");
    }

    @Override
    public AgendamentoState concluido() {
        throw new AgendamentoStateException("Personal ausente.");
    }

    @Override
    public AgendamentoState pendentePersonalConcluir() {
        throw new AgendamentoStateException("Personal ausente.");
    }

    @Override
    public AgendamentoState canceladoPersonal() {
        throw new AgendamentoStateException("Personal ausente.");
    }

    @Override
    public AgendamentoState canceladoCliente() {
        throw new AgendamentoStateException("Personal ausente.");
    }

    @Override
    public AgendamentoState ausenciaPersonal() {
        return this;
    }

    @Override
    public AgendamentoState ausenciaCliente() {
        throw new AgendamentoStateException("Personal ausente.");
    }
}
