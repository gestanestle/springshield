package com.krimo.springshield.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.springshield.dto.ResponseObject;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.security.SecureRandom;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class Utils {

    private Utils() {}

    private static final int CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateSerialCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(SECURE_RANDOM.nextInt(CHARACTERS.length())));
        }
        return stringBuilder.toString();
    }

    @SneakyThrows
    public static void tooManyRequests(HttpServletResponse response) {
        OutputStream out = response.getOutputStream();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(429);
        new ObjectMapper().writeValue(out, new ResponseObject(429, "Server received too many requests. Try again later.", null));
    }
}
