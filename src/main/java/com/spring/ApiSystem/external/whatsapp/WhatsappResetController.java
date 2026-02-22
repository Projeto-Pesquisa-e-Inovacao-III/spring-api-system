package com.spring.ApiSystem.external.whatsapp;


import com.spring.ApiSystem.external.whatsapp.request.ReqResetPassword;
import com.spring.ApiSystem.external.whatsapp.request.ReqSendResetCode;
import com.spring.ApiSystem.external.whatsapp.request.ReqVerifyCode;
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

}
