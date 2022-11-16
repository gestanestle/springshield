package com.krimo.BackendService.user.entity.service;

import com.krimo.BackendService.exception.RequestException;
import com.krimo.BackendService.user.entity.model.User;
import com.krimo.BackendService.user.entity.model.UserRole;
import com.krimo.BackendService.user.entity.repository.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepo userRepo;
    @Autowired
    private UserServiceImpl userServiceImplTest;

    User user;

    @BeforeEach
    void setUp() {
        userServiceImplTest = new UserServiceImpl(userRepo);

        user = new User(
                "swift.taylor@gmail.com",
                "password",
                "Swift",
                "Taylor",
                "Alison",
                LocalDate.of(1989, Month.DECEMBER, 13),
                UserRole.USER);

        user.setId(1L);
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    void setUserEnable() {

        // When
        user.setEnabled(true);
        userServiceImplTest.setUserEnable(user);

        // Then
        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(argument.capture());

        boolean captured_Val = argument.getValue().getEnabled();
        assertThat(captured_Val).isTrue();
    }

    @Test
    void saveUser() {

        // When
        userServiceImplTest.saveUser(user);

        // Then
        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(argument.capture());

        User captured_Val = argument.getValue();
        assertThat(captured_Val).isEqualTo(user);
    }

    @Test
    void deleteUser() {

        // When
        when(userRepo.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        userServiceImplTest.deleteUser(user.getEmail());

        // Then
        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
        verify(userRepo).deleteById(argument.capture());

        Long captured_Val = argument.getValue();
        assertThat(captured_Val).isEqualTo(user.getId());

    }

    @Test
    void emailExists() {

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThat(userServiceImplTest.emailExists(user.getEmail())).isTrue();

    }

    @Test
    void getByEmailThrowExceptionWhenNull() {

        when(userRepo.findByEmail(null)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> userServiceImplTest.getByEmail(null))
                .isInstanceOf(RequestException.class)
                .hasMessageContaining("Email doesn't exist.");

    }

    @Test
    void emailInstances() {

        // When
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        // Then
        assertThat(userServiceImplTest.emailInstances(user.getEmail())).isEqualTo(0L);

        // When
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        // Then
        assertThat(userServiceImplTest.emailInstances(user.getEmail())).isEqualTo(1L);
    }
}