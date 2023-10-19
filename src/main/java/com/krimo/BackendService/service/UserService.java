package com.krimo.BackendService.service;

import com.krimo.BackendService.dto.UserDTO;
import com.krimo.BackendService.model.User;

public interface UserService {
    User getUser(String auth);
    UserDTO displayUser(String auth);
    void updateUser(String auth, UserDTO userDTO);
    void deleteUser(String auth);
}

