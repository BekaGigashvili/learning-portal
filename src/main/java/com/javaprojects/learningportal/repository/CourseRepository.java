package com.javaprojects.learningportal.repository;

import com.javaprojects.learningportal.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByName(String name);
    List<Course> findByNameContainingIgnoreCase(String name);
}
