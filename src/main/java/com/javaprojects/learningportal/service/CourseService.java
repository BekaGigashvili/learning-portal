package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.Course;
import com.javaprojects.learningportal.model.Lesson;
import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    @Transactional
    public void enrollStudent(User user, Course course) {
        course.getEnrolledStudents().add(user);
    }

    public Set<User> getEnrolledStudents(Long courseId) {
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getEnrolledStudents();
    }

    public Set<Lesson> getCourseLessons(Long courseId) {
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getLessons();
    }
}
