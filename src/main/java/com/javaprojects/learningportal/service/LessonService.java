package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.course.Course;
import com.javaprojects.learningportal.model.course.Lesson;
import com.javaprojects.learningportal.model.course.LessonResponse;
import com.javaprojects.learningportal.model.course.Quiz;
import com.javaprojects.learningportal.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
    public Lesson getLesson(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }
    @Transactional
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public LessonResponse addQuiz(Long lessonId, Quiz quiz, User instructor) throws AccessDeniedException {
        Lesson lesson = getLesson(lessonId);
        Course course = lesson.getCourse();
        if(!course.getInstructor().getEmail().equals(instructor.getEmail())){
            throw new AccessDeniedException("You are not the owner of this course");
        }
        quiz.setLesson(lesson);
        lesson.getQuizzes().add(quiz);
        lessonRepository.save(lesson);
        return LessonResponse.builder()
                .title(lesson.getTitle())
                .videoURL(lesson.getVideoURL())
                .courseName(lesson.getCourse().getName())
                .quizNames(lesson.getQuizzes()
                        .stream()
                        .map(Quiz::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
