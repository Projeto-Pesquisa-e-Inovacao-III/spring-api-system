package com.spring.ApiSystem.domain.admin.exception;

public class AdminNaoExisteException extends RuntimeException {
    public AdminNaoExisteException(String message) {
        super(message);
    }
}
