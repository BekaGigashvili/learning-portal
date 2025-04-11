package com.javaprojects.learningportal.controller.auth;

import com.javaprojects.learningportal.model.auth.BlacklistedToken;
import com.javaprojects.learningportal.repository.BlacklistedTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LogoutController {
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token is missing or invalid");
        }
        String token = authHeader.substring(7);
        if(!blacklistedTokenRepository.existsByToken(token)) {
            blacklistedTokenRepository.save(
                    BlacklistedToken.builder()
                            .token(token)
                            .blacklistedAt(Instant.now())
                            .build()
            );
        }
        return ResponseEntity.ok("Successfully logged out");
    }
}
