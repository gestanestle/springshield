package com.krimo.BackendService.user.update;

import com.krimo.BackendService.requestbody.UserObject;


public interface UpdateService {

    void updateUser(String email, UserObject userObject);

}

