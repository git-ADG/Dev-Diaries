package com.example.dev_diaries.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();

        for(FieldError error : exception.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }

        APIError apiError = new APIError(HttpStatus.BAD_REQUEST.value(), "Validation Failed");
        apiError.setValidationErrors(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<APIError> handleNotFoundException(EntityNotFoundException exception){
        APIError apiError = new APIError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIError> handleRuntimeException(RuntimeException exception){
        APIError apiError = new APIError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
