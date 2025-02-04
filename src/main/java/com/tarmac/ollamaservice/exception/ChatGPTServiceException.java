package com.tarmac.ollamaservice.exception;

import org.springframework.http.HttpStatus;

public class ChatGPTServiceException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ChatGPTServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ChatGPTServiceException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
