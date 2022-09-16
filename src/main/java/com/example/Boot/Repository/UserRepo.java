package com.example.Boot.Repository;

import com.example.Boot.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Modifying
    void enabled(Integer id);

    User findByVerificationCode(String verificationCode);
}
