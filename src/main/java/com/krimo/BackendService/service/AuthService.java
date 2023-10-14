package com.krimo.BackendService.service;

import com.krimo.BackendService.dto.CodeDTO;
import com.krimo.BackendService.dto.UserDTO;
import com.krimo.BackendService.model.User;

public interface AuthService {

    Long signUp(UserDTO userDTO);
    void activate(Long id, CodeDTO codeDTO);
    void sendActivation(Long id);
    User authorize(String header);

}

