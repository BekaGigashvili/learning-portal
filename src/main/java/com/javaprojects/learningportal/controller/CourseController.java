package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.Lesson;
import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/lessons/{courseId}")
    public Set<Lesson> getCourseLessons(@PathVariable Long courseId) {
        return courseService.getCourseLessons(courseId);
    }
    @GetMapping("/students/{courseId}")
    public Set<User> getEnrolledUsers(@PathVariable Long courseId) {
        return courseService.getEnrolledStudents(courseId);
    }
}
