package com.javaprojects.learningportal.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {
    private String name;
    private int score;
    private List<String> questions;
    private List<String> answers;
}
