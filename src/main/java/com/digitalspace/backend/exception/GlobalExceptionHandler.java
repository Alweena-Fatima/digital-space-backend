
package com.digitalspace.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(
            RuntimeException exception) {

        String message = exception.getMessage();

        // Avoid returning an empty error message.
        if (message == null || message.isBlank()) {
            message = "Something went wrong. Please try again.";
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;

        // Return 404 when the requested room does not exist.
        if ("Room not found.".equals(message)) {
            status = HttpStatus.NOT_FOUND;
        }

        // Return 500 for unexpected errors, not validation errors.
        // Prefer custom exception classes for production-grade handling.
        return ResponseEntity
                .status(status)
                .body(Map.of("message", message));
    }
}
