package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.VerificationToken;
import com.javaprojects.learningportal.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    public void deleteBuUser(User user){
        verificationTokenRepository.deleteByUser(user);
    }

    public VerificationToken findByUser(User user){
        return verificationTokenRepository.findByUser(user);
    }

    public void saveToken(VerificationToken verificationToken){
        verificationTokenRepository.save(verificationToken);
    }
}
