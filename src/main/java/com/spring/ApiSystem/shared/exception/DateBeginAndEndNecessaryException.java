package com.spring.ApiSystem.shared.exception;

public class DateBeginAndEndNecessaryException extends RuntimeException {
    public DateBeginAndEndNecessaryException(String dateNecessary, String dateProvided) {
        super( dateNecessary + " deve ser fornecida quando " + dateProvided + " é fornecida");
    }
}
