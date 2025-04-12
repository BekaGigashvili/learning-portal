package com.javaprojects.learningportal.repository;

import com.javaprojects.learningportal.model.course.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
