package com.conexa.backend.scheduling.infrastructure.configuration;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionMessageBuilder.class)
    public ResponseEntity<ExceptionResponse> handleCustomExceptions(ExceptionMessageBuilder ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(new ExceptionResponse(ex.getTitle(), ex.getMessage()));
    }

    public record ExceptionResponse(String title, String message) {
    }
}
