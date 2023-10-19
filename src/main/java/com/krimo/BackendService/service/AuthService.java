package com.krimo.BackendService.service;

import com.krimo.BackendService.dto.CodeDTO;
import com.krimo.BackendService.dto.UserDTO;
import com.krimo.BackendService.model.User;

public interface AuthService {

    void signUp(UserDTO userDTO);
    void activate(String email, CodeDTO codeDTO);
    void sendActivation(String email);
    User authorize(String header);

}

