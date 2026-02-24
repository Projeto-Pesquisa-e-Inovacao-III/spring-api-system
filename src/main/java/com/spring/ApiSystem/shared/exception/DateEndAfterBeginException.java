package com.spring.ApiSystem.shared.exception;

public class DateEndAfterBeginException extends RuntimeException {
    public DateEndAfterBeginException(String dateEnd, String dateBegin) {
        super(dateEnd + " deve ser após " + dateBegin);
    }
}
