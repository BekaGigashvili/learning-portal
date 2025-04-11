package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.*;
import com.javaprojects.learningportal.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
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
    @GetMapping("/search")
    public List<CourseResponse> getCoursesByName(@RequestParam String name){
        return courseService.getCoursesByName(name);
    }
    @GetMapping
    public List<CourseResponse> getAllCourses(){
        return courseService.getAllCourses();
    }
    @PostMapping("/delete/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public String deleteCourse(Authentication authentication,
                               @PathVariable Long courseId) throws AccessDeniedException {
        User instructor = (User) authentication.getPrincipal();
        return courseService.deleteCourse(courseId, instructor.getEmail());
    }

    @PutMapping("/{courseId}/add-lesson")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseResponse addLesson(Authentication authentication,
                                    @PathVariable Long courseId,
                                    @RequestBody Lesson lesson) throws AccessDeniedException {
        User instructor = (User) authentication.getPrincipal();
        return courseService.addLesson(courseId, lesson, instructor.getEmail());
    }

    @PutMapping("/{courseId}/{lessonId}/delete-lesson")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public CourseResponse deleteLesson(Authentication authentication,
                                       @PathVariable Long courseId,
                                       @PathVariable Long lessonId) throws AccessDeniedException {
        User instructor = (User) authentication.getPrincipal();
        return courseService.deleteLesson(courseId, lessonId, instructor.getEmail());
    }
}
