package com.krimo.BackendService.registration.service;

import com.krimo.BackendService.requestbody.VerificationTokenObject;

public interface ActivationService {
    void verify(VerificationTokenObject verificationTokenObject);
    void requestNewToken(String email);
}
