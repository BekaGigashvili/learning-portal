package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.RegistrationRequest;
import com.javaprojects.learningportal.model.VerificationToken;
import com.javaprojects.learningportal.service.EmailService;
import com.javaprojects.learningportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest request){
        try{
            String response = userService.register(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
    @GetMapping("/verify")
    public String verifyAndEnableUser(@RequestParam("token") String token){
        return emailService.verifyEmail(token);
    }
}
