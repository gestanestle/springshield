package com.krimo.BackendService.token;

import com.krimo.BackendService.user.entity.model.User;
import com.krimo.BackendService.user.entity.model.UserRole;
import com.krimo.BackendService.user.entity.repository.UserRepo;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class VerificationTokenRepoTest {

    @Autowired
    private UserRepo userRepoTest;
    @Autowired
    private VerificationTokenRepo repoTest;

    User user;
    VerificationToken verificationToken;

    @BeforeEach
    void setUp() {
        user = new User( "swift.taylor@gmail.com",
                "password",
                "Swift",
                "Taylor",
                "Alison",
                LocalDate.of(1989, Month.DECEMBER, 13),
                UserRole.USER);

        userRepoTest.save(user);
    }

    @AfterEach
    void tearDown() {
        user = null;
        verificationToken = null;
        userRepoTest.deleteAll();
    }

    @Test
    void findByToken() {
        // Given
        String token = UUID.randomUUID().toString();
        verificationToken = new VerificationToken(
                token, LocalDateTime.now().plusHours(2), user);

        // When
        repoTest.save(verificationToken);

        // Then
        boolean expected = repoTest.findByToken(token).isPresent();
        assertThat(expected).isTrue();
    }

    @Test
    void findByUserAndToken() {
        // Given
        String token = UUID.randomUUID().toString();
        verificationToken = new VerificationToken(
                token, LocalDateTime.now().plusHours(2), user);

        // When
        repoTest.save(verificationToken);

        // Then
        boolean expected = repoTest.findByUserAndToken(user, token).isPresent();
        assertThat(expected).isTrue();
    }

    @Test
    void deleteByUser() {

        // Given
        String token = UUID.randomUUID().toString();
        verificationToken = new VerificationToken(
                token, LocalDateTime.now().plusHours(2), user);

        // When
        repoTest.save(verificationToken);

        // Then
        boolean isPresent = repoTest.findByToken(token).isPresent();
        assertThat(isPresent).isTrue();

        repoTest.deleteByUser(user);
        boolean isStillPresent = repoTest.findByToken(token).isPresent();
        assertThat(isStillPresent).isFalse();

    }
}