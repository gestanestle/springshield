package com.krimo.BackendService.dto;

import java.time.LocalDate;

public record UserDTO(

        String lastName,
        String firstName,
        String middleName,
        LocalDate birthdate,
        String email,
        String password
) {
}
