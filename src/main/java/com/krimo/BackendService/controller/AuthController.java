package com.krimo.BackendService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.BackendService.dto.CodeDTO;
import com.krimo.BackendService.dto.UserDTO;
import com.krimo.BackendService.model.User;
import com.krimo.BackendService.service.AuthService;
import com.krimo.BackendService.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("api/v2/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody UserDTO userDTO) {
        Long id = authService.signUp(userDTO);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<Object> activate(@PathVariable("id") Long id, @RequestBody CodeDTO codeDTO) {
        authService.activate(id, codeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/resend/{id}")
    public ResponseEntity<Object> resendActivation(@PathVariable("id") Long id) {
        authService.sendActivation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/token/refresh")
    public void getNewTokens(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String header = request.getHeader(AUTHORIZATION);

        User user = authService.authorize(header);

        Map<String, String> tokens = Utils.createTokens(request.getRequestURL().toString(), user);

        OutputStream out = response.getOutputStream();
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(out, tokens);
    }
}
