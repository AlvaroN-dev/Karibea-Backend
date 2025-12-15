package com.microservice.identity.infrastructure.adapters;

import com.microservice.identity.domain.port.out.EmailServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Email Service Adapter for sending emails.
 * Implements EmailServicePort with logging (can be replaced with actual email
 * service).
 * Follows hexagonal architecture by adapting email infrastructure to domain.
 *
 *  Replace with actual email service implementation (e.g., JavaMailSender,
 * SendGrid, AWS SES)
 */
@Component
public class EmailServiceAdapter implements EmailServicePort {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceAdapter.class);

    @Override
    public void sendVerificationEmail(String to, String token) {
        // Implement actual email sending
        logger.info("Sending verification email to: {} with token: {}", to, token);
        logger.info("Verification link: http://localhost:8080/api/auth/verify?token={}", token);
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        // Implement actual email sending
        logger.info("Sending password reset email to: {} with token: {}", to, token);
        logger.info("Reset link: http://localhost:8080/api/auth/reset-password?token={}", token);
    }

    // Implement actual email sending
    @Override
    public void sendWelcomeEmail(String to, String username) {
        logger.info("Sending welcome email to: {} for user: {}", to, username);
        logger.info("Welcome message: Hello {}, welcome to our platform!", username);
    }
}
