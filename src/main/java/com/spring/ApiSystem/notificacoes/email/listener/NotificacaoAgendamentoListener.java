package com.spring.ApiSystem.notificacoes.email.listener;

import com.spring.ApiSystem.agendamento.Agendamento;
import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.eventos.agendamentos.AgendamentoListener;
import com.spring.ApiSystem.notificacoes.email.dto.Email;
import com.spring.ApiSystem.notificacoes.email.service.EmailService;
import com.spring.ApiSystem.personal.Personal;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoAgendamentoListener implements AgendamentoListener {

    private final EmailService emailService;

    public NotificacaoAgendamentoListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onAgendamentoCreated(Agendamento agendamento) {

        Aluno aluno = agendamento.getAluno();
        Personal personal = agendamento.getPersonal();

        Email emailAluno = new Email(
                aluno.getEmail(),
                "Confirmação de Agendamento",
                String.format("Olá %s,\n\nSeu agendamento com o personal %s para data %s às %s foi enviado para aprovação.\n\nObrigado por escolher nossos serviços.",
                        aluno.getNome(),
                        personal.getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                        )
        );

        emailService.enviarEmail(emailAluno);

        Email emailPersonal = new Email(
                personal.getEmail(),
                "Novo Agendamento Recebido",
                String.format("Olá %s,\n\nVocê recebeu um novo agendamento do aluno %s para data %s às %s. Por favor, revise e aprove ou recuse o agendamento.\n\nAtenciosamente,\nEquipe da Plataforma.",
                        personal.getNome(),
                        aluno.getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                )
        );

        emailService.enviarEmail(emailPersonal);
    }




}