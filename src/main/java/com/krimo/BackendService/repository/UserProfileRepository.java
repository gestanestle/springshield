package com.krimo.BackendService.repository;

import com.krimo.BackendService.model.User;
import com.krimo.BackendService.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT u FROM UserProfile u WHERE u.user=?1")
    Optional<UserProfile> findByUser(User user);
    @Modifying
    @Query("DELETE FROM UserProfile u WHERE u.user=?1")
    void deleteByUser(User user);

}
