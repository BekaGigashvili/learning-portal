package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.Course;
import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.repository.CourseRepository;
import com.javaprojects.learningportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    @Transactional
    public String enrollInCourse(Long courseId, Long userId) {
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(user.getEnrolledCourses().contains(course)) {
            return "User already enrolled in this course";
        }
        user.getEnrolledCourses().add(course);
        courseService.enrollStudent(user, course);
        userRepository.save(user);
        return "enrolled in course";
    }

    public Set<Course> getEnrolledCourses(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getEnrolledCourses();
    }


}
