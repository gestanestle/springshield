package com.krimo.BackendService.registration.controller;

import com.krimo.BackendService.requestbody.VerificationTokenObject;
import com.krimo.BackendService.registration.service.ActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class receives the JSON body from the client for the /api/v1/activation
 * endpoint. The method inside this class call the activation services
 * responsible for processing the data sent by the client.
 */

@RestController
@RequestMapping("/api/v1/activation")
@RequiredArgsConstructor
public class ActivationController {

    private final ActivationService activationService;

    @PostMapping
    public ResponseEntity<VerificationTokenObject> activateAccount(@RequestBody VerificationTokenObject verificationTokenObject) {
        activationService.verify(verificationTokenObject);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path="/new/{email}")
    public ResponseEntity<Object> requestNewActivationToken(@PathVariable("email") String userEmail) {
        activationService.requestNewToken(userEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
