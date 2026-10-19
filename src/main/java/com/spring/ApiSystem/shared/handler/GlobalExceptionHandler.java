package com.spring.ApiSystem.shared.handler;

import com.spring.ApiSystem.shared.exception.CustomApiException;
import com.spring.ApiSystem.shared.handler.reponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Captura erros de validação dos DTOs (@Valid, @NotNull, @NotBlank)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2. Captura erros de parâmetros incorretos na URL (ex: erro de tipo ou data no GET)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        String mensagem = String.format("O parâmetro '%s' recebeu o valor '%s', que é inválido.", ex.getName(), ex.getValue());
        error.put("error", mensagem);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 3. Captura erros de parsing de JSON no Body (ex: formato de data inválido no POST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        String mensagem = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "Corpo da requisição inválido ou malformatado";
        error.put("error", mensagem);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 4. Captura exceptions customizadas da sua API
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ErrorResponse> handleCustomApiException(CustomApiException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getStatus().value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }

    // 5. Captura RuntimeExceptions genéricas garantindo que NUNCA retorne "Exception: null"
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();

        // Se getMessage() for null, pega o nome da classe ou a causa
        String mensagem = ex.getMessage();
        if (mensagem == null && ex.getCause() != null) {
            mensagem = ex.getCause().getMessage();
        }
        if (mensagem == null) {
            mensagem = "Ocorreu um erro interno: " + ex.getClass().getSimpleName();
        }

        error.put("error", mensagem);

        // Printa a pilha de erro no console do console/terminal do Spring Boot
        ex.printStackTrace();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}