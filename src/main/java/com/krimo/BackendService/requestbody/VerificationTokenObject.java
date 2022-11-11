package com.krimo.BackendService.requestbody;

import lombok.*;

/**
 * This class binds the request body from the client into a class.
 */

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class VerificationTokenObject {

    private final String email;
    private final String token;

}
