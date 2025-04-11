package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.Course;
import com.javaprojects.learningportal.model.StripeRequest;
import com.javaprojects.learningportal.model.StripeResponse;
import com.javaprojects.learningportal.model.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StripeService {
    @Value("${stripe.secretKey}")
    private String secretKey;
    private final CourseService courseService;

    public StripeResponse checkout(StripeRequest stripeRequest, User user) {
        Stripe.apiKey=secretKey;

        Course course = courseService.getCourse(stripeRequest.getCourseId());

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(course.getName()).build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("USD")
                .setUnitAmount(course.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(priceData)
                .setQuantity(1L)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode((SessionCreateParams.Mode.PAYMENT))
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .putMetadata("userId", String.valueOf(user.getId()))
                .putMetadata("courseId", String.valueOf(stripeRequest.getCourseId()))
                .putMetadata("paymentMethod", stripeRequest.getPaymentMethodType().toString())
                .addLineItem(lineItem)
                .build();
        Session session;
        try{
            session = Session.create(params);
            return StripeResponse.builder()
                    .status("success")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        }catch (StripeException e){
            System.out.println(e.getMessage());
            return StripeResponse.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }
    }
}
