package com.javaprojects.learningportal.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeRequest {
    private Long courseId;
    private PaymentMethodType paymentMethodType;
}
