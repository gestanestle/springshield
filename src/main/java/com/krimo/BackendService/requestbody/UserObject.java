package com.krimo.BackendService.requestbody;

import lombok.*;

import java.time.LocalDate;

/**
 * This class binds the request body from the client into a class.
 */

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class UserObject {

    private final String email;
    private final String password;
    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final LocalDate birthDate;


}
