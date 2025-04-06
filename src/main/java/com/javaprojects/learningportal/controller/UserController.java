package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.Course;
import com.javaprojects.learningportal.service.UserService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/enroll/{courseId}/{userId}")
    public String enrollInCourse(@PathVariable Long courseId,
                                 @PathVariable Long userId) {
        return userService.enrollInCourse(courseId, userId);
    }
    @GetMapping("/courses/{userId}")
    public Set<Course> getEnrolledCourses(@PathVariable Long userId) {
        return userService.getEnrolledCourses(userId);
    }


}
