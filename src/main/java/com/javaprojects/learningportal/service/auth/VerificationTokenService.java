package com.javaprojects.learningportal.service.auth;

import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.auth.VerificationToken;
import com.javaprojects.learningportal.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    public void deleteByUser(User user){
        verificationTokenRepository.deleteByUser(user);
    }

    public VerificationToken findByUser(User user){
        return verificationTokenRepository.findByUser(user);
    }

    public void saveToken(VerificationToken verificationToken){
        verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken findByToken(String token){
        return verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }
}
