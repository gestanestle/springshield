package com.krimo.BackendService.user.entity.repository;

import com.krimo.BackendService.user.entity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepo extends JpaRepository<User,Long>{

    @Query("SELECT u from User u WHERE u.email = ?1")
    Optional<User> findByEmail (String email);

}
