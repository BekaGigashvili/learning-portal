package com.javaprojects.learningportal.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class RegistrationRequest {
    private final String firstName;
    private final String lastName;
    @Email
    private final String email;
    private final String password;
}
