package com.javaprojects.learningportal.controller;

import com.javaprojects.learningportal.model.order.StripeRequest;
import com.javaprojects.learningportal.model.order.StripeResponse;
import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductCheckoutController {

    private final StripeService stripeService;

    @PostMapping("/checkout")
    @Transactional
    public ResponseEntity<StripeResponse> checkout(@RequestBody StripeRequest request,
                                                   Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        StripeResponse response = stripeService.checkout(request, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
