package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.Course;
import com.javaprojects.learningportal.model.RegistrationRequest;
import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.VerificationToken;
import com.javaprojects.learningportal.repository.UserRepository;
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

    @Transactional
    public String enrollInCourse(Long courseId, Long userId) {
        Course course = courseService.getCourse(courseId);
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(user.getEnrolledCourses().contains(course)) {
            return "User already enrolled in this course";
        }
        user.getEnrolledCourses().add(course);
        courseService.enrollStudent(user, course);
        userRepository.save(user);
        return "enrolled in course";
    }

    public Set<Course> getEnrolledCourses(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getEnrolledCourses();
    }

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
                verificationTokenService.deleteBuUser(existingUser);
                user = existingUser;
            }
        }else{
            user.setEmail(email);
            user.setFirstName(request.getFistName());
            user.setLastName(request.getLastName());
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
}
