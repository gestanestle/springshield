package com.krimo.BackendService.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

import static com.krimo.BackendService.utils.Message.USER_DNE;


@ControllerAdvice
public class ApiExceptionHandler {

    /**
     * This method handles the exception thrown from the classes in this project.
     *
     * @param e                contains the exception message
     * @return ResponseEntity  the exception and HTTP code
     */
    @ExceptionHandler(value = {RequestException.class})
    public ResponseEntity<Object> handleRequestException(RequestException e) {

        ApiException apiException = new ApiException(e.getMessage(), e.getStatus(), ZonedDateTime.now(ZoneId.of("Asia/Manila")));

        return new ResponseEntity<>(apiException, e.getStatus());
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElement() {
        ApiException apiException = new ApiException(USER_DNE.message, HttpStatus.BAD_REQUEST, ZonedDateTime.now(ZoneId.of("Asia/Manila")));

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

}
