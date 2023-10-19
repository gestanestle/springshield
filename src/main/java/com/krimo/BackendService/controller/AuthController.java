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
        authService.signUp(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/activate/{email}")
    public ResponseEntity<Object> activate(@PathVariable("email") String email, @RequestBody CodeDTO codeDTO) {
        authService.activate(email, codeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/resend/{email}")
    public ResponseEntity<Object> resendActivation(@PathVariable("email") String email) {
        authService.sendActivation(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/token/refresh")
    public void getNewTokens(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String header = request.getHeader(AUTHORIZATION);

        User user = authService.authorize(header);

        Map<String, String> tokens = Utils.createAccessToken(request.getRequestURL().toString(), user);

        OutputStream out = response.getOutputStream();
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(out, tokens);
    }
}
