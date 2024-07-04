package com.krimo.springshield.exception;

import org.springframework.http.HttpStatus;

public class RequestException extends RuntimeException {

    private final HttpStatus status;
    public RequestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
