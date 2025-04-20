package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.*;
import com.javaprojects.learningportal.model.auth.RegistrationRequest;
import com.javaprojects.learningportal.model.auth.VerificationToken;
import com.javaprojects.learningportal.model.course.Course;
import com.javaprojects.learningportal.model.course.CourseResponse;
import com.javaprojects.learningportal.repository.UserRepository;
import com.javaprojects.learningportal.service.auth.EmailService;
import com.javaprojects.learningportal.service.auth.VerificationTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CourseService courseService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;

    @Transactional
    public UserProfile getUserProfile(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return UserProfile.builder()
                .firstName(managedUser.getFirstName())
                .lastName(managedUser.getLastName())
                .email(managedUser.getEmail())
                .photoUrl(managedUser.getPhotoUrl())
                .enrolledCourses(managedUser.getEnrolledCourses()
                        .stream()
                        .map(courseService::getCourseResponse)
                        .collect(Collectors.toList()))
                .role(managedUser.getRole())
                .build();
    }

    public User getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public ResponseEntity<String> enrollInCourse(Long courseId, Long userId) {
        Course course = courseService.getCourse(courseId);
        User user = getUserById(userId);
        if (user.getEnrolledCourses().contains(course)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already enrolled");
        }
        user.getEnrolledCourses().add(course);
        courseService.enrollStudent(user, course);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("User enrolled in this course");
    }

    public List<CourseResponse> getEnrolledCourses(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getEnrolledCourses().stream()
                .map(courseService::getCourseResponse)   // Map each Course to CourseResponse
                .collect(Collectors.toList());
    }

    @Transactional
    public String register(RegistrationRequest request) throws MessagingException {
        String email = request.getEmail();
        Optional<User> optUser = userRepository.findByEmail(email);
        User user = new User();
        if (optUser.isPresent()) {
            User existingUser = optUser.get();
            if (existingUser.isEnabled()) {
                throw new RuntimeException("Email already registered!");
            }
            VerificationToken existingVerificationToken = verificationTokenService
                    .findByUser(existingUser);
            if (existingVerificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                verificationTokenService.deleteByUser(existingUser);
                user = existingUser;
            }
        } else {
            user.setEmail(email);
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setRole(request.getRole());
            String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
            user.setEnabled(false);
            userRepository.save(user);
        }
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken
                .builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();
        verificationTokenService.saveToken(verificationToken);
        return emailService.sendEmail(user.getEmail(), token);
    }

    @Transactional
    public String verifyAndEnableUser(String token) {
        VerificationToken databaseToken = verificationTokenService.findByToken(token);
        if (databaseToken.getConfirmedAt() != null) {
            return "Email already verified";
        }
        if (databaseToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Verification link expired";
        }
        databaseToken.setConfirmedAt(LocalDateTime.now());
        verificationTokenService.saveToken(databaseToken);
        User user = databaseToken.getUser();
        user.setEnabled(true);
        return "Email verified!";
    }
}
