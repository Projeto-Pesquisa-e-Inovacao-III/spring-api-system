package com.spring.ApiSystem.domain.notificacao.exception;

import com.spring.ApiSystem.shared.exception.CustomApiException;
import org.springframework.http.HttpStatus;

public class NotificacaoNaoEncontradaException extends CustomApiException {
    public NotificacaoNaoEncontradaException(Long id) {
        super("Notificação não encontrada com ID: " + id, HttpStatus.NOT_FOUND);
    }
}
