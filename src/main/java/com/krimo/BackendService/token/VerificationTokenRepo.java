package com.krimo.BackendService.token;

import com.krimo.BackendService.user.entity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

    @Query("SELECT t from VerificationToken t WHERE t.token = ?1")
    Optional<VerificationToken> findByToken(String token);

    @Query("SELECT t FROM VerificationToken t WHERE t.user=?1 AND t.token=?2")
    Optional<VerificationToken> findByUserAndToken(User user, String token);
    @Modifying
    @Query("DELETE FROM VerificationToken t WHERE t.user=?1")
    void deleteByUser(User user);

}
