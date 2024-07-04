package com.krimo.springshield.utils;

public enum Message {

    USER_NOT_FOUND("User with email %s not found."),
    USER_DNE("User does not exist."),
    EMAIL_DNE("Email does not exist."),
    EMAIL_ALREADY_TAKEN("Invalid request. Email has been already taken."),
    EMAIL_SUB("Confirm your email address"),
    EMAIL_BODY("""
                Please enter this activation code to get started:\s
                %s\s
                \s
                Verification codes expire after two hours.""")
    ;


    public final String message;
    private Message(String message) {
        this.message = message;
    }

}
