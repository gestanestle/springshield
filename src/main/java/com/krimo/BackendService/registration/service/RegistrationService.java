package com.krimo.BackendService.registration.service;

import com.krimo.BackendService.requestbody.UserObject;

public interface RegistrationService {
    void register(UserObject account);
}
