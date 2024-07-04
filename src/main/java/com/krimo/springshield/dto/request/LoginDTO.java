package com.krimo.springshield.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public record LoginDTO(
        @NotEmpty(message = "This field cannot be empty.")
        @Email(message = "Invalid email")
        String email,

        @NotEmpty(message = "This filed cannot be empty.")
        @Size(min = 2, message = "Invalid password")
        String password
) {
}
