package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.*;
import com.javaprojects.learningportal.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final LessonsService lessonsService;

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
    private CourseResponse getCourseResponse(Course course) {
        return CourseResponse.builder()
                .name(course.getName())
                .description(course.getDescription())
                .thumbnailURL(course.getThumbnailURL())
                .instructorEmail(course.getInstructor().getEmail())
                .price(course.getPrice())
                .lessonTitles(course.getLessons()
                        .stream()
                        .map(Lesson::getTitle)
                        .toList())
                .build();
    }

    public CourseResponse createCourse(CourseRequest request, User instructor) {
        Course course = Course.builder()
                .name(request.getName())
                .description(request.getDescription())
                .thumbnailURL(request.getThumbnailURL())
                .instructor(instructor)
                .price(request.getPrice())
                .build();
        courseRepository.save(course);
        return getCourseResponse(course);
    }

    @Transactional
    public CourseResponse addLesson(Long courseId, Lesson lesson, String instructorEmail) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        lesson.setCourse(course);
        course.getLessons().add(lesson);
        return getCourseResponse(course);
    }
}
