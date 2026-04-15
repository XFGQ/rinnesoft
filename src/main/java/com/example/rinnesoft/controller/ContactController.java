package com.example.rinnesoft.controller;

import com.example.rinnesoft.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RateLimitService rateLimitService;

    @PostMapping
    public ResponseEntity<String> sendMessage(
        @RequestBody Map<String, String> payload,
        HttpServletRequest request
    ) {
        String clientIp = request.getRemoteAddr();
        String name = payload.get("name");
        String email = payload.get("email");
        String messageBody = payload.get("message");

        if (name == null || name.trim().length() <= 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                "Name must be longer than 3 characters."
            );
        }

        if (email == null || !email.contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                "Invalid email address."
            );
        }

        if (messageBody == null || messageBody.trim().length() <= 30) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                "Message must be longer than 30 characters."
            );
        }

        if (!rateLimitService.isAllowed(clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                "Rate limit exceeded."
            );
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("furkanduman1925@gmail.com");
            message.setTo("info@rinnesoft.com");
            message.setSubject("Portfolio Contact: " + payload.get("subject"));

            String content =
                "Name: " +
                name +
                "\n" +
                "Email: " +
                email +
                "\n\n" +
                "Message:\n" +
                messageBody;

            message.setText(content);
            mailSender.send(message);

            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Failed"
            );
        }
    }
}
