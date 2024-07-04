package com.krimo.springshield.controller;

import com.krimo.springshield.dto.ResponseObject;
import com.krimo.springshield.dto.request.CodeDTO;
import com.krimo.springshield.dto.request.SignupDTO;
import com.krimo.springshield.model.User;
import com.krimo.springshield.security.utils.JWTUtils;
import com.krimo.springshield.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v3/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseObject> signup(@Valid @RequestBody SignupDTO signupDTO) {
        authService.signUp(signupDTO);
        return new ResponseEntity<>(
                new ResponseObject(
                        201,
                        "Signup successful.",
                        null
                )
                , HttpStatus.CREATED);
    }

    @PostMapping("/activate")
    public ResponseEntity<Object> activate(@Valid @RequestBody CodeDTO codeDTO) {
        authService.activate(codeDTO);
        return new ResponseEntity<>(
                new ResponseObject(
                        200,
                        "Activation successful.",
                        null
                )
                , HttpStatus.OK);
    }

    @GetMapping("/resend")
    public ResponseEntity<Object> resendActivation(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        User user = authService.authorize(header);

        authService.sendActivation(user.getEmail());

        return new ResponseEntity<>(
                new ResponseObject(
                        200,
                        "Activation email resent.",
                        null
                )
                , HttpStatus.OK);
    }

    @GetMapping("/token")
    public ResponseEntity<ResponseObject> refresh(HttpServletRequest request) {

        String header = request.getHeader(AUTHORIZATION);
        User user = authService.validate(header);
        String token = JWTUtils.createAccessToken(request.getRequestURL().toString(), user);

        return new ResponseEntity<>(
                new ResponseObject(
                        200,
                        "Access Token refreshed.",
                        Map.of("accessToken", token)
                ),
                HttpStatus.OK
        );
    }
}
