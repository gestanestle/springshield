package com.krimo.BackendService.user.entity.service;

import com.krimo.BackendService.user.entity.model.User;

public interface UserEmailService {
    boolean emailExists(String email);
    User getByEmail(String email);
    long emailInstances (String email);

}
