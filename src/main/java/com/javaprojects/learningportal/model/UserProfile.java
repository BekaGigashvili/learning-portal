package com.javaprojects.learningportal.model;

import com.javaprojects.learningportal.model.course.CourseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;
    private List<CourseResponse> enrolledCourses;
    private Role role;
}
