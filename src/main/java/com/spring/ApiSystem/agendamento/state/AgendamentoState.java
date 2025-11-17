package com.spring.ApiSystem.agendamento.state;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;

public interface AgendamentoState {

    AgendamentoStatus getSituacao();

    AgendamentoState aprovado();
    AgendamentoState pendenteClienteAprovacao();
    AgendamentoState pendentePersonalAprovacao();
    AgendamentoState concluido();
    AgendamentoState pendentePersonalConcluir();
    AgendamentoState canceladoPersonal();
    AgendamentoState canceladoCliente();
    AgendamentoState ausenciaPersonal();
    AgendamentoState ausenciaCliente();
}
