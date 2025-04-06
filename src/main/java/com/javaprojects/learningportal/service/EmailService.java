package com.javaprojects.learningportal.service;

import com.javaprojects.learningportal.model.User;
import com.javaprojects.learningportal.model.VerificationToken;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final VerificationTokenService verificationTokenService;
    private final UserService userService;

    public String sendEmail(String to, String token) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject("Email Verification");
        helper.setText("Please verify your email address by clicking on the link below: " +
                "http://localhost:8080/verify?token=" + token);
        mailSender.send(mimeMessage);
        return "Verification link sent";
    }

    @Transactional
    public String verifyEmail(String token){
        VerificationToken databaseToken = verificationTokenService.findByToken(token);
        if(databaseToken.getConfirmedAt() != null){
            return "Email already verified";
        }
        if(databaseToken.getExpiryDate().isBefore(LocalDateTime.now())){
            return "Verification link expired";
        }
        return userService.verifyAndEnableUser(databaseToken);
    }
}
