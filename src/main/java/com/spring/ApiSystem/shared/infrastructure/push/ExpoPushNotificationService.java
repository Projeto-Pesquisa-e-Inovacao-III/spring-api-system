package com.spring.ApiSystem.shared.infrastructure.push;

import com.spring.ApiSystem.domain.usuario.push.UsuarioPushTokenService;
import com.spring.ApiSystem.shared.infrastructure.websocket.dto.NotificationPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpoPushNotificationService {

    private static final Logger log = LoggerFactory.getLogger(ExpoPushNotificationService.class);

    private final RestClient restClient;
    private final UsuarioPushTokenService usuarioPushTokenService;
    private final String expoPushUrl;
    private final boolean pushEnabled;

    public ExpoPushNotificationService(
            RestClient.Builder restClientBuilder,
            UsuarioPushTokenService usuarioPushTokenService,
            @Value("${app.push.expo.url:https://exp.host/--/api/v2/push/send}") String expoPushUrl,
            @Value("${app.push.expo.enabled:true}") boolean pushEnabled
    ) {
        this.restClient = restClientBuilder.build();
        this.usuarioPushTokenService = usuarioPushTokenService;
        this.expoPushUrl = expoPushUrl;
        this.pushEnabled = pushEnabled;
    }

    public void sendToUser(String email, NotificationPayload payload) {
        if (!pushEnabled) {
            log.debug("Push desativado via config; envio ignorado para email={}", email);
            return;
        }

        List<String> tokens = usuarioPushTokenService.findActiveTokensByEmail(email);
        if (tokens.isEmpty()) {
            log.debug("Nenhum push token ativo para email={}", email);
            return;
        }

        for (String token : tokens) {
            sendMessageToToken(email, token, payload);
        }
    }

    private void sendMessageToToken(String email, String token, NotificationPayload payload) {
        Map<String, Object> data = new HashMap<>();
        if (payload.id() != null) {
            data.put("id", payload.id());
        }
        data.put("targetEmail", email);
        data.put("type", payload.type());
        data.put("createdAt", payload.createdAt().toString());
        if (payload.dadosAdicionais() != null) {
            data.put("dadosAdicionais", payload.dadosAdicionais());
            data.put("data", payload.dadosAdicionais());
            if (payload.dadosAdicionais().contains("\"recipient\":\"aluno\"")) {
                data.put("recipient", "aluno");
            } else if (payload.dadosAdicionais().contains("\"recipient\":\"personal\"")) {
                data.put("recipient", "personal");
            }
        }

        Map<String, Object> message = new HashMap<>();
        message.put("to", token);
        message.put("title", payload.title());
        message.put("body", payload.message());
        message.put("sound", "default");
        message.put("priority", "high");
        message.put("channelId", "agendamentos");
        message.put("data", data);

        try {
            String responseBody = restClient.post()
                    .uri(expoPushUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(message)
                    .retrieve()
                    .body(String.class);

            if (responseBody != null && responseBody.contains("DeviceNotRegistered")) {
                log.warn("Expo indicou token inválido para email={}, token={}", email, token);
                usuarioPushTokenService.deactivateToken(token);
            }
        } catch (RestClientResponseException ex) {
            log.error("Falha ao enviar push via Expo. email={}, status={}, body={}",
                    email, ex.getStatusCode(), ex.getResponseBodyAsString(), ex);

            if (ex.getResponseBodyAsString() != null && ex.getResponseBodyAsString().contains("DeviceNotRegistered")) {
                usuarioPushTokenService.deactivateToken(token);
            }
        } catch (RestClientException ex) {
            log.error("Erro de cliente ao enviar push via Expo. email={}", email, ex);
        }
    }
}
