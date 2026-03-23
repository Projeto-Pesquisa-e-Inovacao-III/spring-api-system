package com.spring.ApiSystem.shared.handler;



import com.spring.ApiSystem.domain.disponibilidade.exception.CantDeactivateDisponibildadeException;
import com.spring.ApiSystem.shared.exception.CustomApiException;
import com.spring.ApiSystem.shared.handler.reponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Captura a exception personalizada
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("Exception", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ErrorResponse> handleCustomApiException(CustomApiException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getStatus().value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CantDeactivateDisponibildadeException.class)
    public ResponseEntity<Map<String, Object>> handleCantDeactivateDisponibildadeException(CantDeactivateDisponibildadeException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("Exception", ex.getMessage());
        error.put("Agendamentos", ex.getAgendamentoList());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
