package com.krimo.BackendService.user.entity.repository;

import com.krimo.BackendService.user.entity.model.User;
import com.krimo.BackendService.user.entity.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepoTest {

    @Autowired
    private UserRepo repoTest;

    @Test
    void findByEmail() {

        // Given
        String email = "swift.taylor@gmail.com";
        User user =  new User (
                email,
                "password",
                "Swift",
                "Taylor",
                "Alison",
                LocalDate.of(1989, Month.DECEMBER, 13),
                UserRole.USER
        );

        // when
        repoTest.save(user);

        // Then
        boolean expected = repoTest.findByEmail(email).isPresent();
        assertThat(expected).isTrue();

    }
}