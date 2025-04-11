package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.*;
import com.javaprojects.learningportal.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CourseService courseService;

    public OrderResponse createOrder(Long userId,
                                     Long courseId,
                                     PaymentMethodType paymentMethodType) {
        User user = userService.getUserById(userId);
        Course course = courseService.getCourse(courseId);
        Order order = Order.builder()
                .user(user)
                .course(course)
                .orderDate(LocalDateTime.now())
                .totalPrice(course.getPrice())
                .paymentMethodType(paymentMethodType)
                .paymentStatus("PAID")
                .build();
        orderRepository.save(order);

        return OrderResponse.builder()
                .userId(order.getUser().getId())
                .courseId(order.getCourse().getId())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethodType(order.getPaymentMethodType())
                .build();
    }
}
