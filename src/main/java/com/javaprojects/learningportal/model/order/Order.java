package com.javaprojects.learningportal.model.order;

import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.course.Course;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    private LocalDateTime orderDate;
    @Column(nullable = false)
    private BigDecimal totalPrice;
    private String paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;
}
