package com.krimo.springshield.dto;

public record ResponseObject(
        int status,
        String message,
        Object data
) {
}
