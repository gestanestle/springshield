package com.krimo.springshield.exception;

import com.krimo.springshield.dto.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.krimo.springshield.utils.Message.USER_DNE;


@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {RequestException.class})
    public ResponseEntity<ResponseObject> handleRequestException(RequestException e) {
        return new ResponseEntity<>(
                new ResponseObject(
                        e.getStatus().value(),
                        e.getMessage(),
                        null)
                , e.getStatus());
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<ResponseObject> handleNoSuchElement() {
        return new ResponseEntity<>(
                new ResponseObject(
                        400,
                        USER_DNE.message,
                        null
                )
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleInvalidSchema(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(
                new ResponseObject(
                        400,
                        "An error occured.",
                        errors
                )
                , HttpStatus.BAD_REQUEST);
    }
}
