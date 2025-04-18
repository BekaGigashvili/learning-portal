package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.*;
import com.javaprojects.learningportal.model.course.*;
import com.javaprojects.learningportal.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final LessonService lessonService;

    @Transactional
    public void enrollStudent(User user, Course course) {
        course.getEnrolledStudents().add(user);
    }

    public Set<User> getEnrolledStudents(Long courseId) {
        Course course = getCourse(courseId);
        return course.getEnrolledStudents();
    }
    @Transactional
    public boolean isUserEnrolled(Long courseId, User user) {
        Course course = getCourse(courseId);
        for(User enrolledUser : course.getEnrolledStudents()){
            if(enrolledUser.getEmail().equals(user.getEmail())){
                return true;
            }
        }
        return false;
    }

    public Set<LessonResponse> getCourseLessons(Long courseId, User user) throws AccessDeniedException {
        Role role = user.getRole();
        Course course = getCourse(courseId);
        if (user.getRole() == Role.STUDENT && !user.getEnrolledCourses().contains(course)) {
            throw new AccessDeniedException("You are not enrolled in this course");
        }
        return course.getLessons()
                .stream()
                .map(lesson -> lessonService.getLessonResponse(lesson.getId()))
                .collect(Collectors.toSet());
    }

    public Course getCourse(Long courseId) {
        return courseRepository
                .findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public CourseResponse getCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .thumbnailURL(course.getThumbnailURL())
                .instructorEmail(course.getInstructor().getEmail())
                .price(course.getPrice())
                .lessonTitles(course.getLessons()
                        .stream()
                        .sorted((l1, l2) -> Long.compare(l1.getId(), l2.getId())) // sort by ID
                        .map(Lesson::getTitle)
                        .toList())
                .build();
    }

    public List<CourseResponse> getCoursesByName(String name) {
        List<Course> courses = courseRepository.findByNameContainingIgnoreCase(name);
        return courses.stream()
                .map(this::getCourseResponse)
                .collect(Collectors.toList());
    }

    public List<CourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::getCourseResponse)
                .collect(Collectors.toList());
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
    public String deleteCourse(Long courseId, String instructorEmail) throws AccessDeniedException {
        Course course = getCourse(courseId);
        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new AccessDeniedException("You are not the owner of this course!");
        }
        courseRepository.delete(course);
        return "Course deleted";
    }

    @Transactional
    public CourseResponse addLesson(Long courseId, Lesson lesson, String instructorEmail) throws AccessDeniedException {
        Course course = getCourse(courseId);
        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new AccessDeniedException("You are not the owner of this course!");
        }
        lesson.setCourse(course);
        course.getLessons().add(lesson);
        return getCourseResponse(course);
    }

    @Transactional
    public CourseResponse deleteLesson(Long courseId,
                                       Long lessonId,
                                       String instructorEmail) throws AccessDeniedException {
        Course course = getCourse(courseId);
        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new AccessDeniedException("You are not the owner of this course!");
        }
        Lesson lesson = lessonService.getLesson(lessonId);
        course.getLessons().remove(lesson);
        lesson.setCourse(null);
        return getCourseResponse(course);
    }
}
