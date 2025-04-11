package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.PaymentMethodType;
import com.javaprojects.learningportal.service.OrderService;
import com.javaprojects.learningportal.service.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class StripeWebhookController {

    private final OrderService orderService;
    private final UserService userService;

    @Value("${webhook.secretKey}")
    private String endpointSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(HttpServletRequest request) throws IOException {
        System.out.println("Webhook received: " + request.getRequestURI());
        String payload = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        String sigHeader = request.getHeader("Stripe-Signature");

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            System.out.println("✅ Stripe webhook triggered");
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null) {
                Long userId = Long.parseLong(session.getMetadata().get("userId"));
                Long courseId = Long.parseLong(session.getMetadata().get("courseId"));

                userService.enrollInCourse(courseId, userId);
                orderService.createOrder(
                        userId,
                        courseId,
                        PaymentMethodType
                                .valueOf(session.getMetadata()
                                .get("paymentMethod"))
                );
            }
        }

        return ResponseEntity.ok("Webhook received");
    }
}
