package com.krimo.BackendService.token;

import com.krimo.BackendService.email.EmailSenderService;
import com.krimo.BackendService.exception.RequestException;
import com.krimo.BackendService.user.entity.model.User;
import com.krimo.BackendService.user.entity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepo verificationTokenRepo;
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    private static final String EMAIL_SUBJECT = "Confirm your email address";
    private static final String EMAIL_BODY =
            """
                    Please enter this verification code to get started:\s
                    %s\s
                    \s
                    Verification codes expire after two hours.""";

    public void saveToken(VerificationToken token) {
        verificationTokenRepo.save(token);
    }

    public boolean userTokenExists(VerificationToken token) {
        return verificationTokenRepo.findByUserAndToken(token.getUser(), token.getToken()).isPresent();
    }

    public boolean isTokenExpired(VerificationToken token) {
        return LocalDateTime.now().isAfter(token.getExpiresAt());
    }

    public void verifyToken(VerificationToken token) {

        if (!userTokenExists(token)) {
            throw new RequestException("Invalid token");
        }

        VerificationToken userToken = verificationTokenRepo.findByToken(token.getToken()).get();
        if (isTokenExpired(userToken)) {
            throw new RequestException("Token has expired.");
        }

        userService.setUserEnable(token.getUser());
    }

    public void generateToken(User user) {

        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken(
                token, LocalDateTime.now().plusHours(2), user);

        saveToken(verificationToken);

        emailSenderService.sendMaiL(user.getEmail(), EMAIL_SUBJECT, String.format(EMAIL_BODY, token));
    }

    public void deleteToken(User user) {
        verificationTokenRepo.deleteByUser(user);
    }

}
