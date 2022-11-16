package com.krimo.BackendService.token;

import com.krimo.BackendService.email.EmailSenderService;
import com.krimo.BackendService.exception.RequestException;
import com.krimo.BackendService.user.entity.model.User;
import com.krimo.BackendService.user.entity.model.UserRole;
import com.krimo.BackendService.user.entity.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationTokenServiceTest {

    @Mock
    private VerificationTokenRepo tokenRepo;
    @Mock
    private UserService userService;
    @Mock
    private EmailSenderService emailSenderService;
    private VerificationTokenService tokenServiceTest;

    User user = null;
    VerificationToken verificationToken = null;

    @BeforeEach
    void setUp() {
        tokenServiceTest = new VerificationTokenService(tokenRepo, userService, emailSenderService);

        user = new User("swift.taylor@gmail.com",
                "password",
                "Swift",
                "Taylor",
                "Alison",
                LocalDate.of(1989, Month.DECEMBER, 13),
                UserRole.USER);

        String token = UUID.randomUUID().toString();

        verificationToken = new VerificationToken(
                token, LocalDateTime.now().plusHours(2), user);
    }

    @AfterEach
    void tearDown() {
        user = null;
        verificationToken = null;
    }

    void whenNotEmptyToken(){
        when(tokenRepo.findByUserAndToken(user, verificationToken.getToken())).thenReturn(Optional.of(verificationToken));
    }

    void whenEmptyToken(){
        when(tokenRepo.findByUserAndToken(user, verificationToken.getToken())).thenReturn(Optional.empty());
    }

    @Test
    void saveToken() {

        // When
        tokenServiceTest.saveToken(verificationToken);

        // Then
        ArgumentCaptor<VerificationToken> argument = ArgumentCaptor.forClass(VerificationToken.class);
        verify(tokenRepo).save(argument.capture());

        VerificationToken captured_Val = argument.getValue();
        assertThat(captured_Val).isEqualTo(verificationToken);
    }
    private VerificationToken setExpiredToken() {
        verificationToken.setExpiresAt(LocalDateTime.now().minusHours(3));
        return verificationToken;

    }

    @Test
    void userTokenExists() {

        // When
        whenNotEmptyToken();
        // Then
        assertThat(tokenServiceTest.userTokenExists(verificationToken)).isTrue();

        // When
        whenEmptyToken();
        // Then
        assertThat(tokenServiceTest.userTokenExists(verificationToken)).isFalse();
    }

    @Test
    void isTokenExpired() {

        // When
        setExpiredToken();
        // Then
        assertThat(tokenServiceTest.isTokenExpired(verificationToken)).isTrue();
    }

    @Test
    void verifyTokenInvalidTokenError() {

        // When
        whenEmptyToken();

        // Then
        assertThatThrownBy( ()-> tokenServiceTest.verifyToken(verificationToken))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining("Invalid token");
    }

    @Test
    void verifyTokenExpiredError(){

        // When
        whenNotEmptyToken();
        setExpiredToken();
        when(tokenRepo.findByToken(verificationToken.getToken())).thenReturn(Optional.of(verificationToken));

        // Then
        assertThatThrownBy( ()-> tokenServiceTest.verifyToken(verificationToken))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining("Token has expired.");

    }

    @Test
    void verifyTokenSetEnabled(){

        // When
        userService.setUserEnable(verificationToken.getUser());

        // Then
        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userService).setUserEnable(argument.capture());

        User captured_Val = argument.getValue();
        assertThat(captured_Val).isEqualTo(user);
    }

    @Test
    void generateToken() {

        String EMAIL_SUBJECT = "Confirm your email address";
        String EMAIL_BODY =
                """
                        Please enter this verification code to get started:\s
                        %s\s
                        \s
                        Verification codes expire after two hours.""";

        emailSenderService.sendMaiL(user.getEmail(), EMAIL_SUBJECT, String.format(EMAIL_BODY, verificationToken));

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(emailSenderService).sendMaiL(argument.capture(), argument.capture(), argument.capture());

        List<String> capturedValues = argument.getAllValues();
        assertEquals(capturedValues.get(0), user.getEmail());
        assertEquals(capturedValues.get(1), EMAIL_SUBJECT);
        assertEquals(capturedValues.get(2), String.format(EMAIL_BODY, verificationToken));

    }

    @Test
    void deleteToken() {

        tokenServiceTest.deleteToken(user);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(tokenRepo).deleteByUser(argument.capture());

        assertEquals(argument.getValue(), user);
    }
}