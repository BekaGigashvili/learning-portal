package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.course.Question;
import com.javaprojects.learningportal.model.course.QuizResponse;
import com.javaprojects.learningportal.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @PutMapping("/add-question/{quizId}")
    public QuizResponse addQuestion(@PathVariable Long quizId,
                                    @RequestBody Question question,
                                    Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        return quizService.addQuestion(quizId, question, user);
    }
}
