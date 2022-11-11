package com.krimo.BackendService.user.update;

import com.krimo.BackendService.exception.RequestException;
import com.krimo.BackendService.requestbody.UserObject;
import com.krimo.BackendService.security.PasswordEncoder;
import com.krimo.BackendService.token.VerificationTokenService;
import com.krimo.BackendService.user.entity.model.User;
import com.krimo.BackendService.user.entity.service.UserEmailService;
import com.krimo.BackendService.user.entity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateServiceImpl implements UpdateService, DeleteService{

    private final UserService userService;
    private final UserEmailService userEmailService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService tokenService;

    /**
     * This method, which is only available for authenticated users,
     * allows for modification of credentials. If the email is to
     * be changed, another verification token will be sent to the
     * newly entered email address and verified all over again.
     * Until then, the user will not be allowed for logging in
     * until the new email address has been verified.
     * The User ID is still the same.
     * @param email         the current authenticated user
     * @param userObject    the new credentials bound by user object
     */
    @Override
    public void updateUser(String email, UserObject userObject) {

        // Current authenticated user
        User user = userEmailService.getByEmail(email);
        Long user_id = user.getId();

        if (!userObject.getEmail().equals(user.getEmail())) {

            User update = new User(
                    userObject.getEmail(), userObject.getPassword(),
                    userObject.getLastName(), userObject.getFirstName(),
                    userObject.getMiddleName(), userObject.getBirthDate(),
                    user.getUserRole()
            );

            // Check if the new email already exists in database
            if (userEmailService.emailInstances(update.getEmail()) >= 1) {
                throw new RequestException("Email has already been taken.");
            }

            String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(update.getPassword());
            update.setPassword(encodedPassword);

            // Map update object to corresponding entity with User ID
            update.setId(user_id);

            userService.saveUser(update);
            tokenService.generateToken(update);
        }
    }

    /**
     * This method, which is only available for authenticated users,
     * allows for complete deletion of account. The verification
     * token saved in the database must be deleted first before
     * the user itself due to relation constraints.
     * @param email     the current user
     */
    @Override
    public void deleteUser(String email) {
        tokenService.deleteToken(userEmailService.getByEmail(email));
        userService.deleteUser(email);
    }
}
