package com.spring.ApiSystem.agendamento.state;

import com.spring.ApiSystem.agendamento.enums.Situacao;

public interface AgendamentoState {

    Situacao getSituacao();

    AgendamentoState recusado();
    AgendamentoState aceitar();
    AgendamentoState concluido();
    AgendamentoState pendenteCliente();
    AgendamentoState pendentePersonal();
}
