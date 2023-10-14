package com.krimo.BackendService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserProfileDTO(
        @JsonProperty("user") UserDTO userDTO,
        @JsonProperty("profile") ProfileDTO profileDTO
) {
}
