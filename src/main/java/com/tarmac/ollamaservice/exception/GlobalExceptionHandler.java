package com.tarmac.ollamaservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChatGPTServiceException.class)
    public ResponseEntity<?> handleChatGPTServiceException(ChatGPTServiceException ex) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(Map.of("error", ex.getMessage()));
    }

}
