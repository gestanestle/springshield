package com.krimo.BackendService.user.entity.service;

import com.krimo.BackendService.user.entity.model.User;

public interface UserService {
    void setUserEnable(User user);
    void saveUser(User user);
    void deleteUser(String email);
}
