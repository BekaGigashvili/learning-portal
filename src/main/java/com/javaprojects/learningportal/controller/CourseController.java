package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.*;
import com.javaprojects.learningportal.service.CourseService;
import com.sun.jdi.LongValue;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/create")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseResponse createCourse(@RequestBody CourseRequest request,
                               Authentication authentication) {
        User instructor = (User) authentication.getPrincipal();
        return courseService.createCourse(request, instructor);
    }
    @PutMapping("/{courseId}/add-lesson")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseResponse addLesson(Authentication authentication,
                                    @PathVariable Long courseId,
                                    @RequestBody Lesson lesson) {
        User instructor = (User) authentication.getPrincipal();
        return courseService.addLesson(courseId, lesson, instructor.getEmail());
    }
}
