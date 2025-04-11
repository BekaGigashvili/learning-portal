package com.javaprojects.learningportal.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long userId;
    private Long courseId;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private String paymentStatus;
    private String paymentIntentId;
    private PaymentMethodType paymentMethodType;
}
