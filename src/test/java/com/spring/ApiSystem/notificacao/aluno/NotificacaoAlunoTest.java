package com.spring.ApiSystem.notificacao.aluno;

import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.notificacoes.email.dto.Email;
import com.spring.ApiSystem.notificacoes.email.listener.NotificacaoAlunoListener;
import com.spring.ApiSystem.notificacoes.email.service.EmailService;
import com.spring.ApiSystem.usuario.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do NotificacaoAlunoListener")
class NotificacaoAlunoTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificacaoAlunoListener notificacaoAlunoListener;

    @Captor
    private ArgumentCaptor<Email> emailCaptor;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno(
                1L,
                TipoUsuario.ALUNO,
                "João Silva",
                "M",
                LocalDate.of(2000, 1, 15),
                "joao.silva@example.com",
                "salt123",
                "senhaHash123",
                true,
                "/path/to/photo.jpg",
                new ArrayList<>(),
                "12345678900"
        );
    }

    @Test
    @DisplayName("Deve enviar email de boas-vindas quando um aluno é criado")
    void deveEnviarEmailBoasVindasQuandoAlunoCriado() {
        // Act
        notificacaoAlunoListener.onAlunoCreated(aluno);

        // Assert
        verify(emailService, times(1)).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();
        assertThat(emailEnviado).isNotNull();
        assertThat(emailEnviado.destinatario()).isEqualTo("joao.silva@example.com");
        assertThat(emailEnviado.assunto()).isEqualTo("Bem-vindo à Plataforma!");
        assertThat(emailEnviado.corpo()).contains("João Silva");
        assertThat(emailEnviado.corpo()).contains("Seja bem-vindo à nossa plataforma!");
    }

    @Test
    @DisplayName("Deve enviar email para o endereço correto do aluno")
    void deveEnviarEmailParaEnderecoCorretoAluno() {
        // Arrange
        aluno = new Aluno(
                2L,
                TipoUsuario.ALUNO,
                "Maria Oliveira",
                "F",
                LocalDate.of(1995, 5, 20),
                "maria.oliveira@test.com",
                "salt456",
                "senhaHash456",
                true,
                null,
                new ArrayList<>(),
                "98765432100"
        );

        // Act
        notificacaoAlunoListener.onAlunoCreated(aluno);

        // Assert
        verify(emailService).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();
        assertThat(emailEnviado.destinatario()).isEqualTo("maria.oliveira@test.com");
        assertThat(emailEnviado.corpo()).contains("Maria Oliveira");
    }

    @Test
    @DisplayName("Deve chamar o EmailService exatamente uma vez")
    void deveChamarEmailServiceExatamenteUmaVez() {
        // Act
        notificacaoAlunoListener.onAlunoCreated(aluno);

        // Assert
        verify(emailService, times(1)).enviarEmail(any(Email.class));
        verifyNoMoreInteractions(emailService);
    }

    @Test
    @DisplayName("Deve criar email com todos os campos obrigatórios preenchidos")
    void deveCriarEmailComTodosCamposObrigatoriosPreenchidos() {
        // Act
        notificacaoAlunoListener.onAlunoCreated(aluno);

        // Assert
        verify(emailService).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();
        assertThat(emailEnviado.destinatario()).isNotNull().isNotBlank();
        assertThat(emailEnviado.assunto()).isNotNull().isNotBlank();
        assertThat(emailEnviado.corpo()).isNotNull().isNotBlank();
    }


}
