package com.krimo.BackendService.registration.service;

import com.krimo.BackendService.exception.RequestException;
import com.krimo.BackendService.requestbody.VerificationTokenObject;
import com.krimo.BackendService.requestbody.UserObject;
import com.krimo.BackendService.security.PasswordEncoder;
import com.krimo.BackendService.token.VerificationToken;
import com.krimo.BackendService.token.VerificationTokenService;
import com.krimo.BackendService.user.entity.model.User;
import com.krimo.BackendService.user.entity.model.UserRole;
import com.krimo.BackendService.user.entity.service.UserEmailService;
import com.krimo.BackendService.user.entity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * This class processes the business logic for user registration
 * and activation. It implements the abstract methods from the
 * RegistrationService and ActivationService interfaces.
 */

@Service
@RequiredArgsConstructor
public class ServiceImpl implements RegistrationService, ActivationService {

    private final UserService userService;
    private final UserEmailService userEmailService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService tokenService;


    /**
     * This is assuming that the email has been partially validated from the front-end.
     * The account being registered will be mapped to entity User and saved to database.
     * A verification code will be generated and saved as well.
     * It will then be sent to the new user's email address for activation.
     * At this point, the new User is not yet enabled.
     *
     * @param userObject the user object request body
     */
    @Override
    public void register(UserObject userObject) {

        User user = new User(
                userObject.getEmail(), userObject.getPassword(),
                userObject.getLastName(), userObject.getFirstName(),
                userObject.getMiddleName(), userObject.getBirthDate(),
                UserRole.USER
        );

        if (userEmailService.emailExists(user.getEmail())) {
            throw new RequestException("Email has already been taken.");
        }

        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);

        userService.saveUser(user);
        tokenService.generateToken(user);
    }

    /**
     * This method calls the service responsible for verifying the verification
     * token sent by the client. The fields from the parameter are mapped to
     * the Verification Token object to be processed by the Token Service.
     *
     * @param verificationTokenObject the verification object request body
     */
    @Override
    public void verify(VerificationTokenObject verificationTokenObject) {

        // Fetch the credentials provided by the request body from the db
        String token = verificationTokenObject.getToken();
        User user = userEmailService.getByEmail(verificationTokenObject.getEmail());

        tokenService.verifyToken(new VerificationToken(token, user));
    }

    /**
     * This method calls the service responsible for generating new verification
     * token for the requesting User. The email from the parameter is mapped to
     * the matching User from the database and processed by the Token Service.
     * getByEmail() throws the error for us if the email doesn't exist.
     *
     * @param email the User email provided by the client
     */
    @Override
    public void requestNewToken(String email) {
        User user = userEmailService.getByEmail(email);
        tokenService.generateToken(user);
    }
}

