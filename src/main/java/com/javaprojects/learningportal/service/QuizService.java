package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.course.*;
import com.javaprojects.learningportal.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    private Quiz getQuiz(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }
    @Transactional
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public QuizResponse addQuestion(Long quizId, Question question, User instructor) throws AccessDeniedException {
        Quiz quiz = getQuiz(quizId);
        Lesson lesson = quiz.getLesson();
        Course course = lesson.getCourse();

        if(!course.getInstructor().getEmail().equals(instructor.getEmail())){
            throw new AccessDeniedException("You are not the owner of this course!");
        }
        question.setQuiz(quiz);
        quiz.getQuestions().add(question);
        quizRepository.save(quiz);
        return QuizResponse.builder()
                .name(quiz.getName())
                .score(quiz.getScore())
                .questions(quiz.getQuestions()
                        .stream()
                        .map(Question::getQuestion)
                        .collect(Collectors.toList()))
                .answers(quiz.getQuestions()
                        .stream()
                        .map(Question::getAnswer)
                        .collect(Collectors.toList()))
                .build();
    }
}
