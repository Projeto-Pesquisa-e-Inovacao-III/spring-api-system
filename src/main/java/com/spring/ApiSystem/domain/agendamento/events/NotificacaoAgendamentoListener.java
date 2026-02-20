package com.spring.ApiSystem.shared.infrastructure.email.listener;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.agendamento.events.AgendamentoListener;
import com.spring.ApiSystem.shared.infrastructure.email.dto.Email;
import com.spring.ApiSystem.shared.infrastructure.email.service.EmailService;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.enums.TipoUsuario;
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
                String.format("Olá %s,<br><br>Seu agendamento com o personal %s para data %s às %s foi enviado para aprovação.<br><br>Obrigado por escolher nossos serviços.",
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
                String.format("Olá %s,<br><br>Você recebeu um novo agendamento do aluno %s para data %s às %s. Por favor, revise e aprove ou recuse o agendamento.<br><br>Atenciosamente,<br>Equipe da Plataforma.",
                        personal.getNome(),
                        aluno.getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                )
        );

        emailService.enviarEmail(emailPersonal);
    }


    @Override
    public void onReagendamentoCreated(Agendamento agendamento, Usuario usuario) {

        Usuario destinatario = obterDestinatario(agendamento, usuario);

        Email emailSolicitante = new Email(
                usuario.getEmail(),
                "Solicitação de reagendamento de Agendamento",
                String.format("Olá %s,<br><br>Seu Reagendamento com %s para data %s às %s foi enviado para aprovação.<br><br>Obrigado por escolher nossos serviços.",
                        usuario.getNome(),
                        destinatario.getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                )
        );

        emailService.enviarEmail(emailSolicitante);

        Email emailDestinatario = new Email(
                destinatario.getEmail(),
                "Novo Reagendamento Recebido",
                String.format("Olá %s,<br><br>Você recebeu uma solicitação de reagendamento de %s para data %s às %s. Por favor, revise e aprove ou recuse o reagendamento.<br><br>Atenciosamente,<br>Equipe da Plataforma.",
                        destinatario.getNome(),
                        usuario.getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                )
        );

        emailService.enviarEmail(emailDestinatario);

    }

    @Override
    public void onCancelamentoAgendamento(Agendamento agendamento, Usuario usuario) {
        Usuario destinatario = obterDestinatario(agendamento, usuario);

        Email emailCancelamento = new Email(
                destinatario.getEmail(),
                "Agendamento Cancelado",
                String.format("Olá %s,<br><br>Informamos que %s cancelou o agendamento para a data %s às %s.<br><br>Se tiver alguma dúvida, entre em contato conosco.<br><br>Atenciosamente,<br>Equipe da Plataforma.",
                        destinatario.getNome(),
                        usuario.getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                )
        );

        emailService.enviarEmail(emailCancelamento);
    }

    @Override
    public void onConclusaoAgendamento(Agendamento agendamento) {
        Aluno aluno = agendamento.getAluno();
        Personal personal = agendamento.getPersonal();
        Email emailConclusao = new Email(
                aluno.getEmail(),
                "Agendamento Concluído",
                String.format("Olá %s,<br><br>Seu agendamento com %s para data %s às %s foi concluído com sucesso.<br><br>Atenciosamente,<br>Equipe da Plataforma.",
                        aluno.getNome(),
                        personal.getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                )
        );

        emailService.enviarEmail(emailConclusao);
    }

    @Override
    public void onAprovacaoAgendamento(Agendamento agendamento, Usuario usuario) {
        Usuario destinatario = obterDestinatario(agendamento, usuario);

        Email emailAprovacao = new Email(
                destinatario.getEmail(),
                "Agendamento Aprovado",
                String.format("Olá %s,<br><br>Seu agendamento com %s para data %s às %s foi aprovado.<br><br>Atenciosamente,<br>Equipe da Plataforma.",
                        destinatario.getNome(),
                        usuario.getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                )
        );
        emailService.enviarEmail(emailAprovacao);
    }

    @Override
    public void onAusenciaRegistradaPersonal(Agendamento agendamento) {
        Email email = new Email(
                agendamento.getAluno().getEmail(),
                "Registro de Ausência",
                String.format("Olá %s,<br><br>Informamos que %s registrou uma ausência para o agendamento na data %s às %s.<br>O saldo do agendamento retornará à sua conta em breve.<br><br>Por favor, entre em contato para mais informações.<br><br>Atenciosamente,<br>Equipe da Plataforma.",
                        agendamento.getAluno().getNome(),
                        agendamento.getPersonal().getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                )
        );

        emailService.enviarEmail(email);
    }


    @Override
    public void onAusenciaRegistradaAluno(Agendamento agendamento) {
        Email email = new Email(
                agendamento.getAluno().getEmail(),
                "Registro de Ausência",
                String.format("Olá %s,<br><br>Informamos que %s registrou sua ausência para o agendamento na data %s às %s.<br><br>Por favor, entre em contato para mais informações.<br><br>Atenciosamente,<br>Equipe da Plataforma.",
                        agendamento.getAluno().getNome(),
                        agendamento.getPersonal().getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                )
        );

        emailService.enviarEmail(email);
    }

    @Override
    public void onAusenciaRegistradaAlunoJustificado(Agendamento agendamento) {
        Email email = new Email(
                agendamento.getAluno().getEmail(),
                "Justificativa de Ausência Aceita",
                String.format("Olá %s,<br><br>Informamos que %s registrou sua ausência para o agendamento na data %s às %s.<br>O saldo do agendamento retornará à sua conta em breve.<br><br>Atenciosamente,<br>Equipe da Plataforma.",
                        agendamento.getAluno().getNome(),
                        agendamento.getPersonal().getNome(),
                        agendamento.getData().toLocalDate().toString(),
                        agendamento.getData().toLocalTime().toString()
                )
        );

        emailService.enviarEmail(email);
    }


    /**
     * Determina o destinatário da notificação com base no tipo de usuário solicitante.
     * Se o solicitante for ALUNO, o destinatário será o PERSONAL.
     * Se o solicitante for PERSONAL, o destinatário será o ALUNO.
     *
     * @param agendamento o agendamento relacionado
     * @param usuario o usuário solicitante
     * @return o usuário destinatário
     * @throws IllegalArgumentException se o tipo de usuário for nulo ou inválido
     */
    private Usuario obterDestinatario(Agendamento agendamento, Usuario usuario) {
        if(usuario.getTipo() == null){
            throw new IllegalArgumentException("Tipo de usuário não pode ser nulo.");
        }

        if(usuario.getTipo().equals(TipoUsuario.ALUNO)){
            return agendamento.getPersonal();
        } else if (usuario.getTipo().equals(TipoUsuario.PERSONAL)){
            return agendamento.getAluno();
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido para reagendamento.");
        }
    }

}