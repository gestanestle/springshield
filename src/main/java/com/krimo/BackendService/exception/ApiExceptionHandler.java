package com.krimo.BackendService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;


@ControllerAdvice
public class ApiExceptionHandler {

    /**
     * This method handles the exception thrown from the classes in this project.
     *
     * @param e                contains the exception message
     * @return ResponseEntity  the exception and HTTP code
     */
    @ExceptionHandler(value = {RequestException.class})
    public ResponseEntity<Object> handleRequestException (RequestException e) {

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(e.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("Asia/Manila")));

        return new ResponseEntity<>(apiException, badRequest);
    }

}
