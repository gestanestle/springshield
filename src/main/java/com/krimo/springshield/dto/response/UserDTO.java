package com.krimo.springshield.dto.response;

import com.krimo.springshield.model.UserRole;

import java.time.LocalDate;

public record UserDTO(
        Long id,
        String lastName,
        String firstName,
        String middleName,
        LocalDate birthdate,
        String email,
        Boolean isEnabled,
        UserRole role,
        LocalDate createdAt
) {
}
