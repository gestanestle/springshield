package com.krimo.BackendService.repository;

import com.krimo.BackendService.model.ActivationCode;
import com.krimo.BackendService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {

    @Query("SELECT t FROM ActivationCode t WHERE t.user=?1 AND t.code=?2")
    Optional<ActivationCode> findByUserAndCode(User user, String code);
    @Modifying
    @Query("DELETE FROM ActivationCode t WHERE t.user=?1")
    void deleteByUser(User user);

}
