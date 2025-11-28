package com.spring.ApiSystem.notificacoes.email.service;

import com.spring.ApiSystem.notificacoes.email.dto.Email;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreply@email.com");
            helper.setTo(email.destinatario());
            helper.setSubject(email.assunto());
            String template = carregaTemplateEmail();
            template = template.replace("${corpo}", email.corpo());

            helper.setText(template, true);
//            mailSender.send(message);

        }catch (Exception exception){
            System.out.println("Falha no envio do email" + exception);
        }

    }

    public String carregaTemplateEmail() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/emailTemplate.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
