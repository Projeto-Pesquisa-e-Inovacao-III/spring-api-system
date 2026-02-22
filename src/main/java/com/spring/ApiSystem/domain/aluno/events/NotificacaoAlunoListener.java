package com.spring.ApiSystem.domain.aluno.events;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.events.AlunosListener;
import com.spring.ApiSystem.shared.infrastructure.email.dto.Email;
import com.spring.ApiSystem.shared.infrastructure.email.service.EmailService;
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
