package com.spring.ApiSystem.notificacao.produtocontratado;
import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.notificacoes.email.dto.Email;
import com.spring.ApiSystem.notificacoes.email.listener.NotificacaoProdutoContratado;
import com.spring.ApiSystem.notificacoes.email.service.EmailService;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificacaoProdutoContratadoTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificacaoProdutoContratado notificacaoProdutoContratado;

    private Aluno aluno;
    private ProdutoExibicao produtoExibicao;
    private ProdutoContratado produtoContratado;
    private Email email;

    @BeforeEach
    void setUp() {
        // Criar objetos de teste
        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setEmail("joao@example.com");

        produtoExibicao = new ProdutoExibicao();
        produtoExibicao.setId(1L);
        produtoExibicao.setTitulo("Pacote Personal");

        produtoContratado = new ProdutoContratado();
        produtoContratado.setId(1L);
        produtoContratado.setAluno(aluno);
        produtoContratado.setProdutoExibicao(produtoExibicao);
    }

    @Test
    void testNotificacaoProdutoContratadoEnviarEmail() {
        // Act
        notificacaoProdutoContratado.onProdutoContratadoCreated(produtoContratado);

        // Assert - Captura o Email enviado
        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(emailService, times(1)).enviarEmail(emailCaptor.capture());

        Email emailEnviado = emailCaptor.getValue();
        assertEquals("joao@example.com", emailEnviado.destinatario());
        assertEquals("Confirmação de Contratação de Produto", emailEnviado.assunto());
        assertTrue(emailEnviado.corpo().contains("João Silva"));
        assertTrue(emailEnviado.corpo().contains("Pacote Personal"));
    }


}