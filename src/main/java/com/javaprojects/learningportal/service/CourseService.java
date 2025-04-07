package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.Course;
import com.javaprojects.learningportal.model.CourseRequest;
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

    public Course getCourse(Long courseId) {
        return courseRepository
                .findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course createCourse(CourseRequest request, User instructor) {
        Course course = Course.builder()
                .name(request.getName())
                .description(request.getDescription())
                .thumbnailURL(request.getThumbnailURL())
                .instructor(instructor)
                .price(request.getPrice())
                .build();
        return courseRepository.save(course);
    }
}
