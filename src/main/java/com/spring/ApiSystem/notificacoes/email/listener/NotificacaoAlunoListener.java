package com.spring.ApiSystem.notificacoes.email.listener;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.eventos.aluno.AlunosListener;
import com.spring.ApiSystem.notificacoes.email.dto.Email;
import com.spring.ApiSystem.notificacoes.email.service.EmailService;
import org.springframework.stereotype.Component;


@Component
public class NotificacaoAlunoListener implements AlunosListener {

    private final EmailService emailService;

    public NotificacaoAlunoListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onAlunoCreated(Aluno aluno) {

        Email email = new Email(
                aluno.getEmail(),
                "Bem-vindo à Plataforma!",
                String.format("Olá %s,<br><br>Seja bem-vindo à nossa plataforma! Estamos felizes com sua presença.<br><br>Atenciosamente,<br>Equipe da Plataforma.",
                        aluno.getNome())
        );

        emailService.enviarEmail(email);
    }
}
