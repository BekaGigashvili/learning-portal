package com.javaprojects.learningportal.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequest {
    private String name;
    private String description;
    private String thumbnailURL;
    private BigDecimal price;
}
