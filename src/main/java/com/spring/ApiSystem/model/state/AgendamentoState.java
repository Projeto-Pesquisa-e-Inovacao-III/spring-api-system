package com.spring.ApiSystem.model.state;

import com.spring.ApiSystem.model.enums.Situacao;

public interface AgendamentoState {

    Situacao getSituacao();

    AgendamentoState recusado();
    AgendamentoState aceitar();
    AgendamentoState concluido();;
    AgendamentoState pendenteCliente();
    AgendamentoState pendentePersonal();
    AgendamentoState reagendar();
}
