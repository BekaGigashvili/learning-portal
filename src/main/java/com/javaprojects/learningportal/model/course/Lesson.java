package com.javaprojects.learningportal.model.course;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String videoURL;
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private Set<Quiz> quizzes = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Lesson(String title, String videoURL) {
        this.title = title;
        this.videoURL = videoURL;
    }
}
