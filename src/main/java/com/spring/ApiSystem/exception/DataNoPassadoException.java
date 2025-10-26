package com.spring.ApiSystem.exception;

public class DataNoPassadoException extends RuntimeException {
    public DataNoPassadoException() {
        super("Data não pode estar no passado");
    }
}
