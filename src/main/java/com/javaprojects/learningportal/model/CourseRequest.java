package com.javaprojects.learningportal.model;

import lombok.Data;

@Data
public class CourseRequest {
    private String name;
    private String description;
    private String thumbnailURL;
    private double price;
}
