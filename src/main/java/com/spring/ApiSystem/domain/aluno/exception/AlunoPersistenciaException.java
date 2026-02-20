package com.spring.ApiSystem.aluno.exception;

public class AlunoPersistenciaException extends RuntimeException {
    public AlunoPersistenciaException(String message) {
        super(message);
    }
    public AlunoPersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}

