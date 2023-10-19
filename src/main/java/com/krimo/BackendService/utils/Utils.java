package com.krimo.BackendService.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.krimo.BackendService.model.User;
import com.krimo.BackendService.security.utils.JWTUtils;

import java.security.SecureRandom;
import java.util.Map;

public class Utils {

    private static final int CODE_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final JWTUtils jwtUtils = new JWTUtils();
    public static String generateSerialCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            stringBuilder.append(CHARACTERS.charAt(SECURE_RANDOM.nextInt(CHARACTERS.length())));
        }
        return stringBuilder.toString();
    }

    public static Map<String, String> createTokens(String url, User user) {

        // Access tokens last for 15 minutes
        String access_token = jwtUtils.createJwt(
                user.getUsername(),
                url,
                user.getAuthorities(),
                900000);

        // Refresh tokens last for 14 days
        String refresh_token = jwtUtils.createJwt(
                user.getUsername(),
                url,
                user.getAuthorities(),
                1209600000);


        return Map.of("access_token", access_token, "refresh_token", refresh_token);
    }

    public static Map<String, String> createAccessToken(String url, User user) {
        // Access tokens last for 15 minutes
        String access_token = jwtUtils.createJwt(
                user.getUsername(),
                url,
                user.getAuthorities(),
                900000);

        return Map.of("access_token", access_token);
    }

    public static DecodedJWT getDecodedJWT(String token) {
        return jwtUtils.decodeJWT(token);
    }



}
