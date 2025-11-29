package com.spring.ApiSystem.externalservice.controller;

import com.infobip.api.WhatsAppApi;
import com.infobip.ApiException;
import com.infobip.model.*;
import com.infobip.model.WhatsAppBulkMessage;
import com.infobip.model.WhatsAppBulkMessageInfo;
import com.infobip.model.WhatsAppMessage;
import com.infobip.model.WhatsAppTemplateDataContent;
import com.infobip.model.WhatsAppTemplateContent;
import com.infobip.model.WhatsAppTemplateUrlButtonContent;


import com.spring.ApiSystem.externalservice.model.CodeVerificationRequest;
import com.spring.ApiSystem.externalservice.model.WhatsappRequest;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/password-reset")
public class WhatsappController {

    private static final Logger logger = LoggerFactory.getLogger(WhatsappController.class);

    @Value("${infobip.whatsapp.sender}")
    private String infobipWhatsappSender;

    @Value("${infobip.whatsapp.template.name}")
    private String templateName;

    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;


    private final WhatsAppApi whatsAppApi;
    private final RestTemplate restTemplate;

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private static final SecureRandom random = new SecureRandom();

    public WhatsappController(WhatsAppApi whatsAppApi, RestTemplate restTemplate) {
        this.whatsAppApi = whatsAppApi;
        this.restTemplate = restTemplate;
    }

    private String generateSixDigitCode() {
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    private String cleanUserIdentifier(String identifier) {
        return identifier.replace("+", "");
    }

    private void sendDiscordLog(String title, String description, String status) {
        try {

            String jsonPayload = String.format(
                    "{\"content\": \"**STATUS: %s**\\n**%s**\\n%s\"}",
                    status, title, description
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            restTemplate.postForLocation(discordWebhookUrl, entity);
            logger.info("Log enviado com sucesso para o Discord.");

        } catch (Exception e) {
            logger.error("Falha ao enviar log para o Discord Webhook: {}", e.getMessage());
        }
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendResetCode(@RequestBody WhatsappRequest request) {
        logger.info("-> Recebida requisição Infobip para ENVIAR código para: {}", request.getTo());

        String recipientNumber = request.getTo().replace("+", "");
        String userIdentifierKey = cleanUserIdentifier(request.getTo());

        logger.info("DEBUG: infobipWhatsappSender injetado como: [{}]", infobipWhatsappSender);

        if (!request.getTo().startsWith("+")) {
            logger.error("Número de telefone deve começar com '+'.");
            return new ResponseEntity<>("Número de telefone deve incluir o código do país (+xx).", HttpStatus.BAD_REQUEST);
        }

        try {
            String newCode = generateSixDigitCode();
            verificationCodes.put(userIdentifierKey, newCode);
            logger.info("Código Infobip gerado e armazenado para {}: {}", request.getTo(), newCode);

            WhatsAppTemplateBodyContent bodyContent = new WhatsAppTemplateBodyContent()
                    .addPlaceholdersItem(newCode);

            WhatsAppTemplateUrlButtonContent urlButtonContent = new WhatsAppTemplateUrlButtonContent()
                    .parameter(String.valueOf(Collections.singletonList(newCode)));

            WhatsAppTemplateDataContent templateDataContent = new WhatsAppTemplateDataContent()
                    .body(bodyContent)
                    .addButtonsItem(urlButtonContent);


            WhatsAppTemplateContent content = new WhatsAppTemplateContent()
                    .templateName(templateName)
                    .language("en")
                    .templateData(templateDataContent);


            WhatsAppMessage singleMessage = new WhatsAppMessage()
                    .from(infobipWhatsappSender)
                    .to(recipientNumber)
                    .content(content)
                    .notifyUrl("https://cylindrical-serfishly-heaven.ngrok-free.dev/api/v1/password-reset/status-callback");


            WhatsAppBulkMessageInfo response = whatsAppApi.sendWhatsAppTemplateMessage(
                    new WhatsAppBulkMessage().addMessagesItem(singleMessage)
            ).execute();

            // Message ID e Log
            String messageId = response.getMessages().getFirst().getMessageId();
            String messageIdSafe = (messageId != null) ? messageId : "N/A";
            logger.info("<- Mensagem Infobip WhatsApp enviada. Message ID: {}", messageId);

            return new ResponseEntity<>(
                    Map.of("message", "Código enviado via Infobip.", "messageId", messageIdSafe),
                    HttpStatus.OK
            );

        } catch (ApiException e) {
            logger.error("<- Erro Infobip ao enviar mensagem: Status {} | Mensagem: {}", e.getCause(), e.getMessage());
            return new ResponseEntity<>("Erro Infobip ao enviar: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("<- Erro Geral ao enviar mensagem: {}", e.getMessage(), e);
            return new ResponseEntity<>("Erro interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody CodeVerificationRequest request) {

        String userIdentifier = request.getUserIdentifier();
        String enteredCode = request.getVerificationCode();
        String userIdentifierKey = cleanUserIdentifier(userIdentifier);

        logger.info("-> Recebida requisição de VERIFICAÇÃO para {}. Código digitado: {}",
                userIdentifierKey, enteredCode);


        if (enteredCode == null || enteredCode.isEmpty()) {

            return new ResponseEntity<>("O código de verificação é obrigatório.", HttpStatus.BAD_REQUEST);
        }


        String storedCode = verificationCodes.get(userIdentifierKey);


        if (storedCode == null) {

            logger.warn("<- Verificação falhou. Nenhum código ativo encontrado para {}.", userIdentifierKey);

            sendDiscordLog(
                    "VERIFICAÇÃO FALHOU: CÓDIGO EXPIRADO/NÃO ENCONTRADO",
                    "Tentativa para " + userIdentifierKey + ". Código digitado: " + enteredCode, // Usa enteredCode
                    "ERRO"
            );
            return new ResponseEntity<>("Nenhum código ativo encontrado.", HttpStatus.NOT_FOUND);
        }


        if (enteredCode.equals(storedCode)) {

            verificationCodes.remove(userIdentifierKey);

            logger.info("<- Verificação bem-sucedida para {}. Código validado.", userIdentifier);

            sendDiscordLog(
                    "VERIFICAÇÃO SUCESSO",
                    "Usuário " + userIdentifierKey + " validou o código com sucesso. **Código: " + storedCode + "**",
                    "SUCESSO"
            );
            return new ResponseEntity<>("Código validado com sucesso. Prossiga para redefinição.", HttpStatus.OK);

        } else {

            logger.warn("<- Verificação falhou para {}. Código digitado incorreto.", userIdentifier);

            sendDiscordLog(
                    "VERIFICAÇÃO FALHOU: CÓDIGO INVÁLIDO",
                    "Usuário " + userIdentifierKey + " falhou. Código incorreto digitado: **" + enteredCode + "**",
                    "FALHA"
            );
            return new ResponseEntity<>("Código de confirmação inválido.", HttpStatus.UNAUTHORIZED);
        }
    }


    // Webhook de Status
    @PostMapping("/status-callback")
    public ResponseEntity<String> handleStatusCallback(@RequestBody Map<String, Object> infobipPayload) {

        // A Infobip envia um array de 'results' contendo os relatórios de entrega (Delivery Reports).

        // Tenta extrair a lista de resultados
        List<Map<String, Object>> results = (List<Map<String, Object>>) infobipPayload.get("results");

        if (results == null || results.isEmpty()) {
            logger.warn("WEBHOOK INFOBIP RECEBIDO - Payload vazio ou formato inesperado.");
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }

        Map<String, Object> firstResult = results.get(0);

        String messageId = (String) firstResult.get("messageId");

        Object statusObj = firstResult.get("status");
        String statusGroupName = "UNKNOWN";

        if (statusObj instanceof Map<?, ?> statusMap) {
            Object groupNameObj = statusMap.get("groupName");
            if (groupNameObj instanceof String) {
                statusGroupName = (String) groupNameObj;
            }
        }

        String errorDescription = (String) firstResult.get("errorDescription");

        logger.info(
                "WEBHOOK INFOBIP RECEBIDO - ID: {}, Status Grupo: {}, Descrição: {}",
                messageId, statusGroupName, errorDescription != null ? errorDescription : "N/A"
        );

        // Os status importantes da Infobip são: PENDING, DELIVERED, CANCELED, REJECTED, UNDELIVERABLE
        if ("DELIVERED".equalsIgnoreCase(statusGroupName)) {
            logger.info("A mensagem {} foi ENTREGUE ao usuário com sucesso.", messageId);

        } else if ("UNDELIVERABLE".equalsIgnoreCase(statusGroupName) || "REJECTED".equalsIgnoreCase(statusGroupName)) {
            logger.error("A mensagem {} FALHOU. Status: {}. Causa: {}", messageId, statusGroupName, errorDescription);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}