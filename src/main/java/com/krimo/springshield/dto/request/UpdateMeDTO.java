package com.krimo.springshield.dto.request;

import java.time.LocalDate;

public record UpdateMeDTO(
        String lastName,
        String firstName,
        String middleName,
        LocalDate birthdate,
        String email,
        String password
) {
}
