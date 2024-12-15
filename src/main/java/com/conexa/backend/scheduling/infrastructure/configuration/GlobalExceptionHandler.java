package com.conexa.backend.scheduling.infrastructure.configuration;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionMessageBuilder.class)
    public ResponseEntity<ExceptionResponse> handleCustomExceptions(ExceptionMessageBuilder ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ExceptionResponse(ex.getTitle(), ex.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(NoHandlerFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse("Not Found", "The requested endpoint does not exist."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse("Internal Server Error", ex.getMessage()));
    }

    public record ExceptionResponse(String title, String message) {}
}
