package com.microservice.identity.domain.port.out;

public interface EmailServicePort {
    void sendVerificationEmail(String to, String token);
    void sendPasswordResetEmail(String to, String token);
    void sendWelcomeEmail(String to, String username);
}
