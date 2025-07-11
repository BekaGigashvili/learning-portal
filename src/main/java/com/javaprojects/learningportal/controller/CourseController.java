package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.*;
import com.javaprojects.learningportal.model.course.*;
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

    @GetMapping("/{courseId}/lessons")
    @PreAuthorize("hasAnyRole('LECTURER', 'STUDENT')")
    public Set<LessonResponse> getCourseLessons(@PathVariable Long courseId,
                                                Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        return courseService.getCourseLessons(courseId, user);
    }
    @GetMapping("/students/{courseId}")
    @PreAuthorize("hasRole('LECTURER')")
    public Set<User> getEnrolledUsers(@PathVariable Long courseId) {
        return courseService.getEnrolledStudents(courseId);
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('LECTURER')")
    public CourseResponse createCourse(@RequestBody CourseRequest request,
                                       Authentication authentication) {
        User instructor = (User) authentication.getPrincipal();
        return courseService.createCourse(request, instructor);
    }
    @GetMapping("/search")
    public List<CourseResponse> getCoursesByName(@RequestParam String name){
        return courseService.getCoursesByName(name);
    }
    @GetMapping("/{courseId}")
    public CourseResponse getCourseById(@PathVariable Long courseId) {
        Course course = courseService.getCourse(courseId);
        return courseService.getCourseResponse(course);
    }
    @PostMapping("/delete/{courseId}")
    @PreAuthorize("hasRole('LECTURER')")
    public String deleteCourse(Authentication authentication,
                               @PathVariable Long courseId) throws AccessDeniedException {
        User instructor = (User) authentication.getPrincipal();
        return courseService.deleteCourse(courseId, instructor.getEmail());
    }

    @PutMapping("/{courseId}/add-lesson")
    @PreAuthorize("hasRole('LECTURER')")
    public CourseResponse addLesson(Authentication authentication,
                                    @PathVariable Long courseId,
                                    @RequestBody Lesson lesson) throws AccessDeniedException {
        User instructor = (User) authentication.getPrincipal();
        return courseService.addLesson(courseId, lesson, instructor.getEmail());
    }

    @PutMapping("/{courseId}/{lessonId}/delete-lesson")
    @PreAuthorize("hasRole('LECTURER')")
    public CourseResponse deleteLesson(Authentication authentication,
                                       @PathVariable Long courseId,
                                       @PathVariable Long lessonId) throws AccessDeniedException {
        User instructor = (User) authentication.getPrincipal();
        return courseService.deleteLesson(courseId, lessonId, instructor.getEmail());
    }
}
