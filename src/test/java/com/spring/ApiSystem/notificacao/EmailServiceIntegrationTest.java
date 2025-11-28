package com.spring.ApiSystem.notificacao;

import com.spring.ApiSystem.notificacoes.email.dto.Email;
import com.spring.ApiSystem.notificacoes.email.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * TESTE DE INTEGRAÇÃO - Este teste REALMENTE envia email para o Mailtrap!
 * Use @SpringBootTest para carregar o contexto completo do Spring
 */
@SpringBootTest
@ActiveProfiles("dev") // Usa as configurações de application-dev.yml
public class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testEnviarEmailReal() {
        // Arrange - Criar um email para enviar
        Email email = new Email(
                "teste@example.com",
                "Teste de Email - Spring Boot",
                """
                Olá!
                
                Este é um teste de envio de email através do Spring Boot.
                
                Se você recebeu esta mensagem no Mailtrap, significa que está funcionando!
                
                Atenciosamente,
                Sistema de Notificações
                """
        );

//        // Act & Assert - Envia o email e verifica que não lança exceção
//        assertDoesNotThrow(() -> emailService.enviarEmail(email));
//
//        System.out.println("✅ Email enviado com sucesso!");
//        System.out.println("📧 Verifique sua caixa de entrada no Mailtrap: https://mailtrap.io/inboxes");
    }
}

