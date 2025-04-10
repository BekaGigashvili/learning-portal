package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.*;
import com.javaprojects.learningportal.model.auth.RegistrationRequest;
import com.javaprojects.learningportal.model.auth.VerificationToken;
import com.javaprojects.learningportal.model.course.Course;
import com.javaprojects.learningportal.repository.UserRepository;
import com.javaprojects.learningportal.service.auth.EmailService;
import com.javaprojects.learningportal.service.auth.VerificationTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CourseService courseService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;

    public User getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Transactional
    public String enrollInCourse(Long courseId, Long userId) {
        Course course = courseService.getCourse(courseId);
        User user = getUserById(userId);
        if(user.getEnrolledCourses().contains(course)) {
            throw new UnsupportedOperationException("User is already enrolled in this course");
        }
        user.getEnrolledCourses().add(course);
        courseService.enrollStudent(user, course);
        return "enrolled in course";
    }

    public Set<Course> getEnrolledCourses(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getEnrolledCourses();
    }
    @Transactional
    public String register(RegistrationRequest request) throws MessagingException {
        String email = request.getEmail();
        Optional<User> optUser = userRepository.findByEmail(email);
        User user = new User();
        if(optUser.isPresent()) {
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
        }else{
            user.setEmail(email);
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setRole(Role.STUDENT);
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
    public String verifyAndEnableUser(String token){
        VerificationToken databaseToken = verificationTokenService.findByToken(token);
        if(databaseToken.getConfirmedAt() != null){
            return "Email already verified";
        }
        if(databaseToken.getExpiryDate().isBefore(LocalDateTime.now())){
            return "Verification link expired";
        }
        databaseToken.setConfirmedAt(LocalDateTime.now());
        verificationTokenService.saveToken(databaseToken);
        User user = databaseToken.getUser();
        user.setEnabled(true);
        return "Email verified!";
    }
}
