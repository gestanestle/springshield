package com.krimo.springshield.controller;

import com.krimo.springshield.dto.ResponseObject;
import com.krimo.springshield.dto.request.UpdateMeDTO;
import com.krimo.springshield.dto.response.UserDTO;
import com.krimo.springshield.model.User;
import com.krimo.springshield.service.AuthService;
import com.krimo.springshield.service.MeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v3/me")
@RequiredArgsConstructor @Slf4j
public class MeController {

    private final AuthService authService;
    private final MeService meService;

    @GetMapping
    public ResponseEntity<ResponseObject> get(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        User user = authService.authorize(header);
        UserDTO reqUserDTO = meService.get(user.getEmail());

        return new ResponseEntity<>(
                new ResponseObject(
                        200,
                        "GET Successful.",
                        reqUserDTO
                )
                , HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseObject> update(@Valid  @RequestBody UpdateMeDTO updateMeDTO, HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        User user = authService.authorize(header);
        meService.update(user.getEmail(), updateMeDTO);

        return new ResponseEntity<>(
                new ResponseObject(
                        200,
                        "UPDATE Successful",
                        null
                ),
                HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        User user = authService.authorize(header);
        meService.delete(user.getEmail());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
