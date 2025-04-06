package com.javaprojects.learningportal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
    @OneToOne(mappedBy = "user")
    private User user;
    @Column(nullable = false)
    private LocalDateTime expiryDate;

}
