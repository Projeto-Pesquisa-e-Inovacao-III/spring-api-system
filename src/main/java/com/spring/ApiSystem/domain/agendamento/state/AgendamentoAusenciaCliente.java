// java
package com.spring.ApiSystem.agendamento.state;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.agendamento.exception.AgendamentoStateException;

public class AgendamentoAusenciaCliente implements AgendamentoState {

    @Override
    public AgendamentoStatus getSituacao() {
        return AgendamentoStatus.AUSENCIA_CLIENTE;
    }

    @Override
    public AgendamentoState aprovado() {
        throw new AgendamentoStateException("Cliente ausente.");
    }

    @Override
    public AgendamentoState pendenteClienteAprovacao() {
        throw new AgendamentoStateException("Cliente ausente.");
    }

    @Override
    public AgendamentoState pendentePersonalAprovacao() {
        throw new AgendamentoStateException("Cliente ausente.");
    }

    @Override
    public AgendamentoState concluido() {
        throw new AgendamentoStateException("Cliente ausente.");
    }

    @Override
    public AgendamentoState pendentePersonalConcluir() {
        throw new AgendamentoStateException("Cliente ausente.");
    }

    @Override
    public AgendamentoState canceladoPersonal() {
        throw new AgendamentoStateException("Cliente ausente.");
    }

    @Override
    public AgendamentoState canceladoCliente() {
        throw new AgendamentoStateException("Cliente ausente.");
    }

    @Override
    public AgendamentoState ausenciaPersonal() {
        throw new AgendamentoStateException("Cliente ausente.");
    }

    @Override
    public AgendamentoState ausenciaCliente() {
        return this;
    }
}
