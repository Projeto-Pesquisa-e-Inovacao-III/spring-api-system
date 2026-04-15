
package com.spring.ApiSystem.domain.agendamento.state;

import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.agendamento.exception.AgendamentoStateException;

public class AgendamentoConcluido implements AgendamentoState {

    @Override
    public AgendamentoStatus getSituacao() {
        return AgendamentoStatus.CONCLUIDO;
    }

    @Override
    public AgendamentoState aprovado() {
        throw new AgendamentoStateException("Agendamento já concluído.");
    }

    @Override
    public AgendamentoState pendenteClienteAprovacao() {
        throw new AgendamentoStateException("Não é possível ter um agendamento pendete.");
    }

    @Override
    public AgendamentoState pendentePersonalAprovacao() {
        throw new AgendamentoStateException("Não é possível ter um agendamento pendente.");
    }

    @Override
    public AgendamentoState concluido() {
        return this;
    }

    @Override
    public AgendamentoState pendentePersonalConcluir() {
        throw new AgendamentoStateException("Agendamento já concluído.");
    }

    @Override
    public AgendamentoState canceladoPersonal() {
        throw new AgendamentoStateException("Não é possível cancelar um agendamento concluído.");
    }

    @Override
    public AgendamentoState canceladoCliente() {
        throw new AgendamentoStateException("Não é possível cancelar um agendamento concluído.");
    }

    @Override
    public AgendamentoState ausenciaPersonal() {
        throw new AgendamentoStateException("Não aplicável em concluído.");
    }

    @Override
    public AgendamentoState ausenciaCliente() {
        throw new AgendamentoStateException("Não aplicável em concluído.");
    }
}
