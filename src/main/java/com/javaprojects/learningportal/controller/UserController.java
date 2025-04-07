package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.Course;
import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public String enrollInCourse(Authentication authentication,
                                 @PathVariable Long courseId) {
        User user = (User) authentication.getPrincipal();
        return userService.enrollInCourse(courseId, user.getId());
    }
    @GetMapping("/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public Set<Course> getEnrolledCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getEnrolledCourses(user.getId());
    }
}
