package com.krimo.springshield.utils;

import java.security.SecureRandom;

public class Utils {

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
}
