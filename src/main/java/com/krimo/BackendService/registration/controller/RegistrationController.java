package com.krimo.BackendService.registration.controller;

import com.krimo.BackendService.requestbody.UserObject;
import com.krimo.BackendService.registration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class receives the JSON body from the client for the /api/v1/registration
 * endpoint. The method inside this class call the registration services
 * responsible for processing the data sent by the client.
 */

@RestController
@RequestMapping(path = "/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;


    @PostMapping
    public ResponseEntity<UserObject> register (@RequestBody UserObject userObject) {
        registrationService.register(userObject);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
