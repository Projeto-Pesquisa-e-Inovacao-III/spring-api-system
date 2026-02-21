package com.spring.ApiSystem.shared.exception;

import org.springframework.http.HttpStatus;

public class CustomApiException extends RuntimeException {
    private final HttpStatus status;

    public CustomApiException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.status = status;
    }

    public CustomApiException(String mensagem) {
        super(mensagem);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
