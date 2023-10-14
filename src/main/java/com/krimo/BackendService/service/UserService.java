package com.krimo.BackendService.service;

import com.krimo.BackendService.dto.UserProfileDTO;
import com.krimo.BackendService.model.User;

public interface UserService {
    User getUser(String auth);
    UserProfileDTO displayUser(String auth);
    void updateUser(String auth, UserProfileDTO userProfileDTO);
    void deleteUser(String auth);
}

