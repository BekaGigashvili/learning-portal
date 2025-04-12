package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.course.LessonResponse;
import com.javaprojects.learningportal.model.course.Quiz;
import com.javaprojects.learningportal.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PutMapping("/add-quiz/{lessonId}")
    public LessonResponse addQuiz(@PathVariable Long lessonId,
                                  @RequestBody Quiz quiz,
                                  Authentication authentication) throws AccessDeniedException {
        User user = (User) authentication.getPrincipal();
        return lessonService.addQuiz(lessonId, quiz, user);
    }
}
