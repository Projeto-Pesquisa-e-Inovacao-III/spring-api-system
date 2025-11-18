package com.spring.ApiSystem.notificacoes;

import com.spring.ApiSystem.agendamento.Agendamento;
import com.spring.ApiSystem.eventos.agendamentos.AgendamentoListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoAgendamentoListener implements AgendamentoListener {
    @Override
    public void onAgendamentoCreated(Agendamento agendamento) {

        System.out.printf("Agendamento criado em: %s, id: %s%n",
                agendamento.getData(), agendamento.getId());
    }


}