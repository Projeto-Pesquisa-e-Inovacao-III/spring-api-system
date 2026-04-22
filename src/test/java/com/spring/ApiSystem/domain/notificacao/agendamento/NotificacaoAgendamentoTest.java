package com.spring.ApiSystem.domain.notificacao.agendamento;
import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.agendamento.events.NotificacaoAgendamentoListener;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.shared.infrastructure.email.dto.Email;
import com.spring.ApiSystem.shared.infrastructure.email.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificacaoAgendamentoTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificacaoAgendamentoListener notificacaoAgendamentoListener;

    private Agendamento agendamento;
    private Aluno aluno;
    private Personal personal;
    private LocalDateTime dataAgendamento;

    @BeforeEach
    void setUp() {
        // Configurando data do agendamento
        dataAgendamento = LocalDateTime.of(2025, 11, 30, 14, 30);

        // Criando aluno mock
        aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setEmail("joao.silva@email.com");
        aluno.getUsuario().addRole(Role.ALUNO);

        // Criando personal mock
        personal = new Personal();
        personal.setNome("Carlos Personal");
        personal.setEmail("carlos.personal@email.com");
        personal.getUsuario().addRole(Role.PERSONAL);

        // Criando agendamento mock
        agendamento = new Agendamento();
        agendamento.setData(dataAgendamento);
        agendamento.setAluno(aluno);
        agendamento.setPersonal(personal);
    }

    @Test
    @DisplayName("Deve enviar emails de notificação quando aluno solicita reagendamento")
    void deveEnviarEmailsQuandoAlunoSolicitaReagendamento() {
        // Arrange
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onReagendamentoCreated(agendamento, aluno.getUsuario());

        // Assert
        verify(emailService, times(2)).enviarEmail(emailCaptor.capture());

        List<Email> emailsEnviados = emailCaptor.getAllValues();

        // Verificando email para o aluno (solicitante)
        Email emailAluno = emailsEnviados.get(0);
        assertEquals("joao.silva@email.com", emailAluno.destinatario());
        assertEquals("Solicitação de reagendamento de Agendamento", emailAluno.assunto());
        assertTrue(emailAluno.corpo().contains("João Silva"));
        assertTrue(emailAluno.corpo().contains("Carlos Personal"));
        assertTrue(emailAluno.corpo().contains("2025-11-30"));
        assertTrue(emailAluno.corpo().contains("14:30"));
        assertTrue(emailAluno.corpo().contains("enviado para aprovação"));

        // Verificando email para o personal (destinatário)
        Email emailPersonal = emailsEnviados.get(1);
        assertEquals("carlos.personal@email.com", emailPersonal.destinatario());
        assertEquals("Novo Reagendamento Recebido", emailPersonal.assunto());
        assertTrue(emailPersonal.corpo().contains("Carlos Personal"));
        assertTrue(emailPersonal.corpo().contains("João Silva"));
        assertTrue(emailPersonal.corpo().contains("2025-11-30"));
        assertTrue(emailPersonal.corpo().contains("14:30"));
        assertTrue(emailPersonal.corpo().contains("solicitação de reagendamento"));
    }

    @Test
    @DisplayName("Deve enviar emails de notificação quando personal solicita reagendamento")
    void deveEnviarEmailsQuandoPersonalSolicitaReagendamento() {
        // Arrange
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onReagendamentoCreated(agendamento, personal.getUsuario());

        // Assert
        verify(emailService, times(2)).enviarEmail(emailCaptor.capture());

        List<Email> emailsEnviados = emailCaptor.getAllValues();

        // Verificando email para o personal (solicitante)
        Email emailPersonal = emailsEnviados.get(0);
        assertEquals("carlos.personal@email.com", emailPersonal.destinatario());
        assertEquals("Solicitação de reagendamento de Agendamento", emailPersonal.assunto());
        assertTrue(emailPersonal.corpo().contains("Carlos Personal"));
        assertTrue(emailPersonal.corpo().contains("João Silva"));
        assertTrue(emailPersonal.corpo().contains("2025-11-30"));
        assertTrue(emailPersonal.corpo().contains("14:30"));
        assertTrue(emailPersonal.corpo().contains("enviado para aprovação"));

        // Verificando email para o aluno (destinatário)
        Email emailAluno = emailsEnviados.get(1);
        assertEquals("joao.silva@email.com", emailAluno.destinatario());
        assertEquals("Novo Reagendamento Recebido", emailAluno.assunto());
        assertTrue(emailAluno.corpo().contains("João Silva"));
        assertTrue(emailAluno.corpo().contains("Carlos Personal"));
        assertTrue(emailAluno.corpo().contains("2025-11-30"));
        assertTrue(emailAluno.corpo().contains("14:30"));
        assertTrue(emailAluno.corpo().contains("solicitação de reagendamento"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário é inválido")
    void deveLancarExcecaoQuandoTipoUsuarioInvalido() {
        // Arrange
        Usuario usuarioInvalido = mock(Usuario.class);
        when(usuarioInvalido.getRoles()).thenReturn(Set.of());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> notificacaoAgendamentoListener.onReagendamentoCreated(agendamento, usuarioInvalido)
        );

        assertEquals("Tipo de usuário não pode ser vazio ou nulo.", exception.getMessage());
        verify(emailService, never()).enviarEmail(any());
    }

    @Test
    @DisplayName("Deve enviar emails com dados corretos do agendamento")
    void deveEnviarEmailsComDadosCorretosDoAgendamento() {
        // Arrange
        LocalDateTime dataEspecifica = LocalDateTime.of(2025, 12, 15, 10, 0);
        agendamento.setData(dataEspecifica);

        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onReagendamentoCreated(agendamento, aluno.getUsuario());

        // Assert
        verify(emailService, times(2)).enviarEmail(emailCaptor.capture());

        List<Email> emailsEnviados = emailCaptor.getAllValues();

        // Verificando que ambos os emails contêm a data e hora corretas
        for (Email email : emailsEnviados) {
            assertTrue(email.corpo().contains("2025-12-15"));
            assertTrue(email.corpo().contains("10:00"));
        }
    }

    @Test
    @DisplayName("Deve enviar email de notificação quando personal aprova agendamento")
    void deveEnviarEmailQuandoPersonalAprovaAgendamento() {
        // Arrange
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onAprovacaoAgendamento(agendamento, personal.getUsuario());

        // Assert
        verify(emailService, times(1)).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();

        // Verificando que o email foi enviado para o aluno (destinatário)
        assertEquals("joao.silva@email.com", emailEnviado.destinatario());
        assertEquals("Agendamento Aprovado", emailEnviado.assunto());
        assertTrue(emailEnviado.corpo().contains("João Silva"));
        assertTrue(emailEnviado.corpo().contains("Carlos Personal"));
        assertTrue(emailEnviado.corpo().contains("2025-11-30"));
        assertTrue(emailEnviado.corpo().contains("14:30"));
        assertTrue(emailEnviado.corpo().contains("foi aprovado"));
    }

    @Test
    @DisplayName("Deve enviar email de notificação quando aluno aprova agendamento")
    void deveEnviarEmailQuandoAlunoAprovaAgendamento() {
        // Arrange
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onAprovacaoAgendamento(agendamento, aluno.getUsuario());

        // Assert
        verify(emailService, times(1)).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();

        // Verificando que o email foi enviado para o personal (destinatário)
        assertEquals("carlos.personal@email.com", emailEnviado.destinatario());
        assertEquals("Agendamento Aprovado", emailEnviado.assunto());
        assertTrue(emailEnviado.corpo().contains("Carlos Personal"));
        assertTrue(emailEnviado.corpo().contains("João Silva"));
        assertTrue(emailEnviado.corpo().contains("2025-11-30"));
        assertTrue(emailEnviado.corpo().contains("14:30"));
        assertTrue(emailEnviado.corpo().contains("foi aprovado"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao aprovar agendamento com tipo de usuário inválido")
    void deveLancarExcecaoAoAprovarComTipoUsuarioInvalido() {
        // Arrange
        Usuario usuarioInvalido = mock(Usuario.class);
        when(usuarioInvalido.getRoles()).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> notificacaoAgendamentoListener.onAprovacaoAgendamento(agendamento, usuarioInvalido)
        );

        assertEquals("Tipo de usuário não pode ser vazio ou nulo.", exception.getMessage());
        verify(emailService, never()).enviarEmail(any());
    }

    @Test
    @DisplayName("Deve enviar email de notificação quando agendamento é concluído")
    void deveEnviarEmailQuandoAgendamentoConcluido() {
        // Arrange
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onConclusaoAgendamento(agendamento);

        // Assert
        verify(emailService, times(1)).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();

        // Verificando que o email foi enviado para o aluno
        assertEquals("joao.silva@email.com", emailEnviado.destinatario());
        assertEquals("Agendamento Concluído", emailEnviado.assunto());
        assertTrue(emailEnviado.corpo().contains("João Silva"));
        assertTrue(emailEnviado.corpo().contains("Carlos Personal"));
        assertTrue(emailEnviado.corpo().contains("2025-11-30"));
        assertTrue(emailEnviado.corpo().contains("14:30"));
        assertTrue(emailEnviado.corpo().contains("foi concluído com sucesso"));
    }

    @Test
    @DisplayName("Deve enviar email quando ausência do personal for registrada")
    void deveEnviarEmailQuandoAusenciaPersonalRegistrada() {
        // Arrange
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onAusenciaRegistradaPersonal(agendamento);

        // Assert
        verify(emailService, times(1)).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();

        // Verificando que o email foi enviado para o aluno
        assertEquals("joao.silva@email.com", emailEnviado.destinatario());
        assertEquals("Registro de Ausência", emailEnviado.assunto());
        assertTrue(emailEnviado.corpo().contains("João Silva"));
        assertTrue(emailEnviado.corpo().contains("Carlos Personal"));
        assertTrue(emailEnviado.corpo().contains("2025-11-30"));
        assertTrue(emailEnviado.corpo().contains("14:30"));
        assertTrue(emailEnviado.corpo().contains("registrou uma ausência"));
        assertTrue(emailEnviado.corpo().contains("saldo do agendamento retornará à sua conta"));
    }

    @Test
    @DisplayName("Deve enviar email quando ausência do aluno for registrada")
    void deveEnviarEmailQuandoAusenciaAlunoRegistrada() {
        // Arrange
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onAusenciaRegistradaAluno(agendamento);

        // Assert
        verify(emailService, times(1)).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();

        // Verificando que o email foi enviado para o aluno
        assertEquals("joao.silva@email.com", emailEnviado.destinatario());
        assertEquals("Registro de Ausência", emailEnviado.assunto());
        assertTrue(emailEnviado.corpo().contains("João Silva"));
        assertTrue(emailEnviado.corpo().contains("Carlos Personal"));
        assertTrue(emailEnviado.corpo().contains("2025-11-30"));
        assertTrue(emailEnviado.corpo().contains("14:30"));
        assertTrue(emailEnviado.corpo().contains("registrou sua ausência"));
    }

    @Test
    @DisplayName("Deve enviar email quando ausência do aluno for registrada e justificada")
    void deveEnviarEmailQuandoAusenciaAlunoRegistradaJustificada() {
        // Arrange
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onAusenciaRegistradaAlunoJustificado(agendamento);

        // Assert
        verify(emailService, times(1)).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();

        // Verificando que o email foi enviado para o aluno
        assertEquals("joao.silva@email.com", emailEnviado.destinatario());
        assertEquals("Justificativa de Ausência Aceita", emailEnviado.assunto());
        assertTrue(emailEnviado.corpo().contains("João Silva"));
        assertTrue(emailEnviado.corpo().contains("Carlos Personal"));
        assertTrue(emailEnviado.corpo().contains("2025-11-30"));
        assertTrue(emailEnviado.corpo().contains("14:30"));
        assertTrue(emailEnviado.corpo().contains("registrou sua ausência"));
        assertTrue(emailEnviado.corpo().contains("saldo do agendamento retornará à sua conta"));
    }

    @Test
    @DisplayName("Deve enviar email de notificação quando aluno cancela agendamento")
    void deveEnviarEmailQuandoAlunoCancelaAgendamento() {
        // Arrange
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onCancelamentoAgendamento(agendamento, aluno.getUsuario());

        // Assert
        verify(emailService, times(1)).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();

        // Verificando que o email foi enviado para o personal (destinatário)
        assertEquals("carlos.personal@email.com", emailEnviado.destinatario());
        assertEquals("Agendamento Cancelado", emailEnviado.assunto());
        assertTrue(emailEnviado.corpo().contains("Carlos Personal"));
        assertTrue(emailEnviado.corpo().contains("João Silva"));
        assertTrue(emailEnviado.corpo().contains("2025-11-30"));
        assertTrue(emailEnviado.corpo().contains("14:30"));
        assertTrue(emailEnviado.corpo().contains("cancelou o agendamento"));
    }

    @Test
    @DisplayName("Deve enviar email de notificação quando personal cancela agendamento")
    void deveEnviarEmailQuandoPersonalCancelaAgendamento() {
        // Arrange
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        // Act
        notificacaoAgendamentoListener.onCancelamentoAgendamento(agendamento, personal.getUsuario());

        // Assert
        verify(emailService, times(1)).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();

        // Verificando que o email foi enviado para o aluno (destinatário)
        assertEquals("joao.silva@email.com", emailEnviado.destinatario());
        assertEquals("Agendamento Cancelado", emailEnviado.assunto());
        assertTrue(emailEnviado.corpo().contains("João Silva"));
        assertTrue(emailEnviado.corpo().contains("Carlos Personal"));
        assertTrue(emailEnviado.corpo().contains("2025-11-30"));
        assertTrue(emailEnviado.corpo().contains("14:30"));
        assertTrue(emailEnviado.corpo().contains("cancelou o agendamento"));
    }


}
