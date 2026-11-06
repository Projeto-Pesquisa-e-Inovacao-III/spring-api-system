package com.spring.ApiSystem.domain.notificacao.exception;

import com.spring.ApiSystem.shared.exception.CustomApiException;
import org.springframework.http.HttpStatus;

public class NotificacaoNaoPertenceAoUsuarioException extends CustomApiException {
    public NotificacaoNaoPertenceAoUsuarioException() {
        super("A notificação informada não pertence ao usuário autenticado.", HttpStatus.FORBIDDEN);
    }
}
