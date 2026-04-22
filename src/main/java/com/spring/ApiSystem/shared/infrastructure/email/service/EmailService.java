package com.spring.ApiSystem.shared.infrastructure.email.service;


import com.spring.ApiSystem.shared.infrastructure.email.dto.Email;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void enviarEmail(Email email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // Use Jakarta Mail 2.1+ types (Standard in SB4)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noreply@email.com");
            helper.setTo(email.destinatario());
            helper.setSubject(email.assunto());

            String template = carregaTemplateEmail();
            String htmlContent = template.replace("${corpo}", email.corpo());

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception exception) {
            throw new RuntimeException("Falha no envio do email: " + exception.getMessage(), exception);
        }

    }

    private String carregaTemplateEmail() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/emailTemplate.html");
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
