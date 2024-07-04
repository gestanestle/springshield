package com.krimo.springshield.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public record CodeDTO(
        @NotEmpty(message = "This filed cannot be empty.")
        @Email(message = "Invalid email")
        String email,

        @NotEmpty(message = "This filed cannot be empty.")
        String code
) {
}
