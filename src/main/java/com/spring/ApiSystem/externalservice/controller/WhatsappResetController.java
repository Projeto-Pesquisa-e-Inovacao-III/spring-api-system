package com.spring.ApiSystem.externalservice.controller;

import com.spring.ApiSystem.externalservice.dto.request.ReqSendResetCode;
import com.spring.ApiSystem.externalservice.dto.request.ReqVerifyCode;
import com.spring.ApiSystem.externalservice.dto.request.ReqResetPassword;
import com.spring.ApiSystem.externalservice.service.WhatsappResetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
public class WhatsappResetController {

    private static final Logger logger = LoggerFactory.getLogger(WhatsappResetController.class);

    private final WhatsappResetService whatsappResetService;

    public WhatsappResetController(WhatsappResetService whatsappResetService) {
        this.whatsappResetService = whatsappResetService;
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody ReqSendResetCode req) {
        try {
            String messageId = whatsappResetService.sendResetCodeViaInfobip(req);
            return ResponseEntity.ok(Map.of("message", "Código enviado via Infobip.", "messageId", messageId));
        } catch (RuntimeException e) {
            logger.error("Erro ao enviar código: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody ReqVerifyCode req) {
        try {
            String token = whatsappResetService.verifyCode(req);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            logger.warn("Falha na verificação: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ReqResetPassword req) {
        try {
            whatsappResetService.resetPassword(req);
            return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso."));
        } catch (RuntimeException e) {
            logger.warn("Erro ao redefinir senha: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/status-callback")
    public ResponseEntity<String> handleStatusCallback(@RequestBody Map<String, Object> infobipPayload) {
        List<Map<String, Object>> results = (List<Map<String, Object>>) infobipPayload.get("results");

        if (results == null || results.isEmpty()) {
            logger.warn("WEBHOOK INFOBIP RECEBIDO - Payload vazio ou formato inesperado.");
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }

        Map<String, Object> firstResult = results.get(0);
        String messageId = (String) firstResult.get("messageId");
        Map<String, Object> statusInfo = (Map<String, Object>) firstResult.get("status");
        String statusGroupName = statusInfo != null ? (String) statusInfo.get("groupName") : "UNKNOWN";
        String errorDescription = (String) firstResult.get("errorDescription");

        logger.info("WEBHOOK INFOBIP RECEBIDO - ID: {}, Status Grupo: {}, Descrição: {}",
                messageId, statusGroupName, errorDescription != null ? errorDescription : "N/A");

        if ("DELIVERED".equalsIgnoreCase(statusGroupName)) {
            logger.info("A mensagem {} foi ENTREGUE ao usuário com sucesso.", messageId);
        } else if ("UNDELIVERABLE".equalsIgnoreCase(statusGroupName) || "REJECTED".equalsIgnoreCase(statusGroupName)) {
            logger.error("A mensagem {} FALHOU. Status: {}. Causa: {}", messageId, statusGroupName, errorDescription);
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
