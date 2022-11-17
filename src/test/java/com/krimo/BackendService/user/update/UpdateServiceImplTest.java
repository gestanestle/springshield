package com.krimo.BackendService.user.update;

import com.krimo.BackendService.requestbody.UserObject;
import com.krimo.BackendService.security.PasswordEncoder;
import com.krimo.BackendService.token.VerificationTokenService;
import com.krimo.BackendService.user.entity.model.User;
import com.krimo.BackendService.user.entity.model.UserRole;
import com.krimo.BackendService.user.entity.service.UserEmailService;
import com.krimo.BackendService.user.entity.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateServiceImplTest {

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
    private UpdateServiceImpl updateServiceImplTest;

    String email;
    User user;
    UserObject userObject;


    @BeforeEach
    void setUp() {

        this.passwordEncoder= new PasswordEncoder();

        email = "swift.taylor@gmail.com";
        user = new User(
                email,
                "password",
                "Swift",
                "Taylor",
                "Alison",
                LocalDate.of(1989, Month.DECEMBER, 13),
                UserRole.USER);

        user.setId(1L);

        userObject = new UserObject(
                "alison@gmail.com",
                "password",
                "Gomez",
                "Selena",
                "Alison",
                LocalDate.of(1989, Month.DECEMBER, 13)
        );

        updateServiceImplTest = new UpdateServiceImpl(userService, userEmailService, passwordEncoder, tokenService);
    }

    @AfterEach
    void tearDown() {
        email = null;
        user = null;
        userObject = null;
    }

    @Test
    void updateUser() {

        // Given
        User update = new User(
                userObject.getEmail(), userObject.getPassword(),
                userObject.getLastName(), userObject.getFirstName(),
                userObject.getMiddleName(), userObject.getBirthDate(),
                user.getUserRole()
        );

        // When
        when(userEmailService.getByEmail(email)).thenReturn(user);

        update.setId(user.getId());
        updateServiceImplTest.updateUser(email, userObject);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userService).saveUser(argument.capture());
        verify(tokenService).generateToken(argument.capture());

        assertThat(argument.getValue()).usingRecursiveComparison()
                .ignoringFields("password").isEqualTo(update);

    }


    @Test
    void deleteUser() {

        when(userEmailService.getByEmail(email)).thenReturn(user);

        updateServiceImplTest.deleteUser(email);

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(tokenService).deleteToken(user);
        verify(userService).deleteUser(argument.capture());

        assertEquals(argument.getValue(), email);

    }
}