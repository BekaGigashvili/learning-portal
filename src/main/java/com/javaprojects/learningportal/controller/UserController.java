package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.UserProfile;
import com.javaprojects.learningportal.model.course.Course;
import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.course.CourseResponse;
import com.javaprojects.learningportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> enrollInCourse(Authentication authentication,
                                                 @PathVariable Long courseId) {
        User user = (User) authentication.getPrincipal();
        return userService.enrollInCourse(courseId, user.getId());
    }
    @GetMapping("/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public List<CourseResponse> getEnrolledCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getEnrolledCourses(user.getId());
    }
    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public UserProfile getUserProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getUserProfile(user);
    }
}
