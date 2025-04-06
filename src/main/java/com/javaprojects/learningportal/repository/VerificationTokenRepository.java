package com.javaprojects.learningportal.repository;

import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(User user);
    VerificationToken findByUser(User user);
}
