package com.javaprojects.learningportal.model.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String name;
    private String description;
    private String thumbnailURL;
    private BigDecimal price;
    private String instructorEmail;
    private List<String> lessonTitles;
}
