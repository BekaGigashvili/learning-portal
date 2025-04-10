package com.javaprojects.learningportal.controller.auth;

import com.javaprojects.learningportal.model.auth.RegistrationRequest;
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
        return userService.verifyAndEnableUser(token);
    }
}
