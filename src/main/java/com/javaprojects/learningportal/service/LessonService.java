package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.Lesson;
import com.javaprojects.learningportal.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
