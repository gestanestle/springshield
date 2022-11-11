package com.krimo.BackendService.user.controller;

import com.krimo.BackendService.requestbody.UserObject;
import com.krimo.BackendService.user.update.DeleteService;
import com.krimo.BackendService.user.update.UpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UpdateService updateService;
    private final DeleteService deleteService;


    @GetMapping
    public ResponseEntity<String> getUser(Authentication auth) {
        return new ResponseEntity<>(auth.getName(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserObject userObject, Authentication auth) {
        updateService.updateUser(auth.getName(), userObject);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUser(Authentication auth) {
        deleteService.deleteUser(auth.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
