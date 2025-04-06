package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.AuthenticationRequest;
import com.javaprojects.learningportal.model.AuthenticationResponse;
import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String email = request.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String password = request.getPassword();
        if(passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(user);
            return AuthenticationResponse
                    .builder()
                    .token(token)
                    .build();
        }else {
            throw new RuntimeException("Wrong password");
        }
    }
}
