package com.krimo.BackendService.controller;
import com.krimo.BackendService.dto.UserDTO;
import com.krimo.BackendService.model.User;
import com.krimo.BackendService.service.AuthService;
import com.krimo.BackendService.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v2/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> showUser(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        User user = authService.authorize(header);
        UserDTO userDTO = userService.displayUser(user.getEmail());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        User user = authService.authorize(header);
        userService.updateUser(user.getEmail(), userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUser(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        User user = authService.authorize(header);
        userService.deleteUser(user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
