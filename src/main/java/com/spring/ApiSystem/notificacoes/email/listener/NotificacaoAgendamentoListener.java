package com.spring.ApiSystem.notificacoes.email.listener;

import com.spring.ApiSystem.agendamento.Agendamento;
import com.spring.ApiSystem.eventos.agendamentos.AgendamentoListener;
import com.spring.ApiSystem.notificacoes.email.dto.Email;
import com.spring.ApiSystem.notificacoes.email.service.EmailService;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoAgendamentoListener implements AgendamentoListener {

    private final EmailService emailService;

    public NotificacaoAgendamentoListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onAgendamentoCreated(Agendamento agendamento) {

        System.out.printf("Agendamento criado em: %s, id: %s%n",
                agendamento.getData(), agendamento.getId());
    }


}