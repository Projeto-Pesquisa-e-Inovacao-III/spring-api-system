package com.spring.ApiSystem.external.whatsapp;

import com.infobip.ApiException;
import com.infobip.api.WhatsAppApi;
import com.infobip.model.WhatsAppBulkMessage;
import com.infobip.model.WhatsAppBulkMessageInfo;
import com.infobip.model.WhatsAppMessage;
import com.infobip.model.WhatsAppTemplateBodyContent;
import com.infobip.model.WhatsAppTemplateContent;
import com.infobip.model.WhatsAppTemplateDataContent;
import com.infobip.model.WhatsAppTemplateUrlButtonContent;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.external.whatsapp.dto.request.ReqResetPassword;
import com.spring.ApiSystem.external.whatsapp.dto.request.ReqSendResetCode;
import com.spring.ApiSystem.external.whatsapp.dto.request.ReqVerifyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WhatsappResetService {

    private static final Logger logger = LoggerFactory.getLogger(WhatsappResetService.class);

    private final UsuarioService usuarioService;
    private final WhatsAppApi whatsAppApi;
    private final RestTemplate restTemplate;

    @Value("${infobip.whatsapp.sender}")
    private String infobipWhatsappSender;

    @Value("${infobip.whatsapp.template.name}")
    private String templateName;

    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;

    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, String> resetTokens = new ConcurrentHashMap<>();

    private static final SecureRandom RANDOM = new SecureRandom();

    public WhatsappResetService(UsuarioService usuarioService, WhatsAppApi whatsAppApi, RestTemplate restTemplate) {
        this.usuarioService = usuarioService;
        this.whatsAppApi = whatsAppApi;
        this.restTemplate = restTemplate;
    }

    // ==========================
    // GERAR E ENVIAR VIA INFOBIP
    // Retorna messageId da Infobip
    // ==========================

    public String sendResetCodeViaInfobip(ReqSendResetCode req) {
        String normalized = normalize(req.pais(), req.ddd(), req.numero());
        if (normalized.isBlank()) throw new RuntimeException("Telefone inválido.");

        String code = String.valueOf(RANDOM.nextInt(900000) + 100000);
        verificationCodes.put(normalized, code);

        try {
            WhatsAppTemplateBodyContent bodyContent = new WhatsAppTemplateBodyContent()
                    .addPlaceholdersItem(code);

            WhatsAppTemplateUrlButtonContent urlButtonContent = new WhatsAppTemplateUrlButtonContent()
                    .parameter(String.valueOf(Collections.singletonList(code)));

            WhatsAppTemplateDataContent templateDataContent = new WhatsAppTemplateDataContent()
                    .body(bodyContent)
                    .addButtonsItem(urlButtonContent);

            WhatsAppTemplateContent content = new WhatsAppTemplateContent()
                    .templateName(templateName)
                    .language("en")
                    .templateData(templateDataContent);

            WhatsAppMessage singleMessage = new WhatsAppMessage()
                    .from(infobipWhatsappSender)
                    .to(normalized)
                    .content(content);

            WhatsAppBulkMessageInfo response = whatsAppApi.sendWhatsAppTemplateMessage(
                    new WhatsAppBulkMessage().addMessagesItem(singleMessage)
            ).execute();

            String messageId = response != null && response.getMessages() != null && !response.getMessages().isEmpty()
                    ? response.getMessages().get(0).getMessageId()
                    : null;

            logger.info("Código gerado e mensagem enviada via Infobip para {} - messageId: {}", normalized, messageId);
            sendDiscordLog("CÓDIGO ENVIADO", "Telefone: " + normalized + " Código: " + code, "SUCESSO");

            return messageId != null ? messageId : "N/A";

        } catch (ApiException e) {
            logger.error("Erro Infobip ao enviar mensagem: {}", e.getMessage(), e);
            sendDiscordLog("ERRO ENVIO INFOBIP", "Telefone: " + normalized + " Erro: " + e.getMessage(), "ERRO");
            throw new RuntimeException("Erro Infobip ao enviar: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Erro ao enviar código via Infobip: {}", e.getMessage(), e);
            sendDiscordLog("ERRO ENVIO INFOBIP", "Telefone: " + normalized + " Erro: " + e.getMessage(), "ERRO");
            throw new RuntimeException("Erro interno ao enviar código.", e);
        }
    }

    // ==========================
    // VALIDAR CÓDIGO
    // ==========================

    public String verifyCode(ReqVerifyCode req) {
        String normalized = normalize(req.pais(), req.ddd(), req.numero());
        String storedCode = verificationCodes.get(normalized);

        if (storedCode == null)
            throw new RuntimeException("Nenhum código ativo encontrado.");

        if (!storedCode.equals(req.verificationCode()))
            throw new RuntimeException("Código inválido.");

        verificationCodes.remove(normalized);

        String resetToken = UUID.randomUUID().toString();
        resetTokens.put(normalized, resetToken);

        sendDiscordLog("VERIFICAÇÃO SUCESSO", "Telefone " + normalized + " validou o código.", "SUCESSO");

        return resetToken;
    }

    // ==========================
    // FINALIZAR RESET DE SENHA
    // ==========================
    public void resetPassword(ReqResetPassword req) {
        String normalized = normalize(req.pais(), req.ddd(), req.numero());

        String storedToken = resetTokens.get(normalized);

        if (storedToken == null)
            throw new RuntimeException("Token não encontrado.");

        if (!storedToken.equals(req.token()))
            throw new RuntimeException("Token inválido.");

        // Busca o usuário usando as partes do telefone (pais, ddd, numero)
        Usuario user = usuarioService.buscarUsuarioPorPaisDddNumero(req.pais(), req.ddd(), req.numero());
        if (user == null)
            throw new RuntimeException("Usuário não encontrado.");

        usuarioService.validarComplexidadeSenha(req.newPassword());

        String lowerNova = req.newPassword() == null ? "" : req.newPassword().toLowerCase();
        if ((user.getEmail() != null && lowerNova.contains(user.getEmail().toLowerCase()))
                || (user.getNome() != null && lowerNova.contains(user.getNome().toLowerCase()))) {
            throw new RuntimeException("A nova senha não pode ser relacionada ao usuário.");
        }

        usuarioService.aplicarSenhaCriptografada(user, req.newPassword());
        usuarioService.salvarUsuario(user);

        resetTokens.remove(normalized);

        sendDiscordLog("RESET DE SENHA", "Senha redefinida para " + normalized, "SUCESSO");
    }

    private String normalize(String pais, String ddd, String numero) {
        String p = pais == null ? "" : pais.replaceAll("\\D", "");
        String d = ddd == null ? "" : ddd.replaceAll("\\D", "");
        String n = numero == null ? "" : numero.replaceAll("\\D", "");
        return (p + d + n).trim();
    }

    private void sendDiscordLog(String title, String description, String status) {
        if (discordWebhookUrl == null || discordWebhookUrl.isBlank()) return;
        try {
            String jsonPayload = String.format("{\"content\": \"**STATUS: %s**\\n**%s**\\n%s\"}", status, title, description);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            restTemplate.postForLocation(discordWebhookUrl, entity);
        } catch (Exception e) {
            logger.warn("Falha ao enviar log para Discord: {}", e.getMessage());
        }
    }

    private Map<String, String> getVerificationCodes() {
        return Map.copyOf(verificationCodes);
    }

    private Map<String, String> getResetTokens() {
        return Map.copyOf(resetTokens);
    }
}
