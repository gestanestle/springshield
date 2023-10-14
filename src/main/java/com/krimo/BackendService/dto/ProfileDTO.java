package com.krimo.BackendService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ProfileDTO (
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("middle_name")
        String middleName,
        LocalDate birthdate
) {
}
