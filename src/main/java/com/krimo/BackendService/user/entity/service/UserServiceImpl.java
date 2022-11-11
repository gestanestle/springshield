package com.krimo.BackendService.user.entity.service;

import com.krimo.BackendService.exception.RequestException;
import com.krimo.BackendService.user.entity.repository.UserRepo;
import com.krimo.BackendService.user.entity.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * This class is the service layer before the user repository. This is
 * the implementation of the methods from the service interfaces.
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService, UserEmailService {

    private final UserRepo userRepo;
    private static final String USER_NOT_FOUND = "User with email %s not found.";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    @Override
    public void setUserEnable(User user) {
        user.setEnabled(true);
        userRepo.save(user);
    }

    @Override
    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Override
    public void deleteUser(String email) {
        userRepo.deleteById(getByEmail(email).getId());
    }

    @Override
    public boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    public User getByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(()-> (new RequestException("Email doesn't exist.")));
    }

    @Override
    public long emailInstances(String email) {
        return userRepo.findByEmail(email).stream().count();
    }
}
