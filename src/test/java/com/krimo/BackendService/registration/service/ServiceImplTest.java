package com.krimo.BackendService.registration.service;

import com.krimo.BackendService.exception.RequestException;
import com.krimo.BackendService.requestbody.UserObject;
import com.krimo.BackendService.requestbody.VerificationTokenObject;
import com.krimo.BackendService.security.PasswordEncoder;
import com.krimo.BackendService.token.VerificationToken;
import com.krimo.BackendService.token.VerificationTokenService;
import com.krimo.BackendService.user.entity.model.User;
import com.krimo.BackendService.user.entity.model.UserRole;
import com.krimo.BackendService.user.entity.service.UserEmailService;
import com.krimo.BackendService.user.entity.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceImplTest {

    @Mock
    UserService userService;
    @Mock
    UserEmailService userEmailService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    VerificationTokenService tokenService;
    @InjectMocks
    @Autowired
    private ServiceImpl serviceImplTest;

    User user;
    UserObject userObject;

    @BeforeEach
    void setUp() {

        this.passwordEncoder= new PasswordEncoder();

        userObject = new UserObject(
                "alison@gmail.com",
                "password",
                "Gomez",
                "Selena",
                "Alison",
                LocalDate.of(1989, Month.DECEMBER, 13)
        );

        user = new User(
                userObject.getEmail(), userObject.getPassword(),
                userObject.getLastName(), userObject.getFirstName(),
                userObject.getMiddleName(), userObject.getBirthDate(),
                UserRole.USER
        );

        serviceImplTest = new ServiceImpl(userService, userEmailService, passwordEncoder, tokenService);
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    void register() {

        serviceImplTest.register(userObject);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userService).saveUser(argument.capture());
        Mockito.verify(tokenService).generateToken(argument.capture());

        assertThat(argument.getValue()).usingRecursiveComparison()
                .ignoringFields("password").isEqualTo(user);
    }

    @Test
    void throwsErrorIfEmailExists() {

        when(userEmailService.emailExists(user.getEmail())).thenReturn(true);

        assertThatThrownBy( () -> serviceImplTest.register(userObject))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining("Email has already been taken.");
    }

    @Test
    void verify() {

        VerificationTokenObject verificationTokenObject = new VerificationTokenObject(user.getEmail(), "token");
        when(userEmailService.getByEmail(user.getEmail())).thenReturn(user);

        serviceImplTest.verify(verificationTokenObject);

        ArgumentCaptor<VerificationToken> argument = ArgumentCaptor.forClass(VerificationToken.class);
        Mockito.verify(tokenService).verifyToken(argument.capture());

        assertThat(argument.getValue()).usingRecursiveComparison()
                .ignoringFields("expiresAt")
                .isEqualTo(new VerificationToken(verificationTokenObject.getToken(), user));
    }

    @Test
    void requestNewToken() {

        when(userEmailService.getByEmail(user.getEmail())).thenReturn(user);

        serviceImplTest.requestNewToken(user.getEmail());

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        Mockito.verify(tokenService).generateToken(argument.capture());

        assertThat(argument.getValue()).isEqualTo(user);
    }

}