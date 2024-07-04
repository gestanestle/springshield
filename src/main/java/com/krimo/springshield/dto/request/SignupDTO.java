package com.krimo.springshield.dto.request;

import javax.validation.constraints.*;
import java.time.LocalDate;

public record SignupDTO(

        @NotEmpty(message =  "This field cannot be empty.")
        @Size(min = 2, message = "Characters must be > 2.")
        String lastName,

        @NotEmpty(message =  "This field cannot be empty.")
        @Size(min = 2, message = "Characters must be > 2.")
        String firstName,

        String middleName,

        @Past(message = "Invalid date.")
        LocalDate birthdate,

        @NotEmpty(message =  "This field cannot be empty.")
        @Email(message = "Invalid email.")
        String email,

        @NotEmpty(message =  "This field cannot be empty.")
        @Size(min = 8, max = 64, message = "Password must be between 8-64 characters.")
        String password
) {
}
