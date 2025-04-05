package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.Course;
import com.javaprojects.learningportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/enroll/{courseId}/{userEmail}")
    public String enrollInCourse(@PathVariable Long courseId,
                                 @PathVariable String userEmail) {
        return userService.enrollInCourse(courseId, userEmail);
    }
    @GetMapping("/courses/{userEmail}")
    public Set<Course> getEnrolledCourses(@PathVariable String userEmail) {
        return userService.getEnrolledCourses(userEmail);
    }
}
