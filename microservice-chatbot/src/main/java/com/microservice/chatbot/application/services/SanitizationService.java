package com.microservice.chatbot.application.services;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Service for sanitizing user input before sending to AI model.
 * Location: application/services - Security and validation service.
 */
@Service
public class SanitizationService {

    // Patterns for sensitive data detection
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "\\+?[0-9]{10,15}");
    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile(
            "\\b(?:\\d{4}[- ]?){3}\\d{4}\\b");
    private static final Pattern SSN_PATTERN = Pattern.compile(
            "\\b\\d{3}-\\d{2}-\\d{4}\\b");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "(?i)(password|contraseña|pwd|pass)\\s*[:=]\\s*\\S+", Pattern.CASE_INSENSITIVE);

    // Replacement placeholders
    private static final String EMAIL_REPLACEMENT = "[EMAIL_REMOVED]";
    private static final String PHONE_REPLACEMENT = "[PHONE_REMOVED]";
    private static final String CREDIT_CARD_REPLACEMENT = "[CARD_REMOVED]";
    private static final String SSN_REPLACEMENT = "[SSN_REMOVED]";
    private static final String PASSWORD_REPLACEMENT = "[PASSWORD_REMOVED]";

    /**
     * Sanitizes user input by removing sensitive data.
     *
     * @param input the raw user input
     * @return sanitized input safe for AI processing
     */
    public String sanitize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String sanitized = input;

        // Remove emails
        sanitized = EMAIL_PATTERN.matcher(sanitized).replaceAll(EMAIL_REPLACEMENT);

        // Remove phone numbers
        sanitized = PHONE_PATTERN.matcher(sanitized).replaceAll(PHONE_REPLACEMENT);

        // Remove credit card numbers
        sanitized = CREDIT_CARD_PATTERN.matcher(sanitized).replaceAll(CREDIT_CARD_REPLACEMENT);

        // Remove SSNs
        sanitized = SSN_PATTERN.matcher(sanitized).replaceAll(SSN_REPLACEMENT);

        // Remove passwords
        sanitized = PASSWORD_PATTERN.matcher(sanitized).replaceAll(PASSWORD_REPLACEMENT);

        // Remove excessive whitespace
        sanitized = sanitized.replaceAll("\\s+", " ").trim();

        return sanitized;
    }

    /**
     * Checks if the input contains sensitive data.
     *
     * @param input the input to check
     * @return true if sensitive data is detected
     */
    public boolean containsSensitiveData(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        return EMAIL_PATTERN.matcher(input).find() ||
                PHONE_PATTERN.matcher(input).find() ||
                CREDIT_CARD_PATTERN.matcher(input).find() ||
                SSN_PATTERN.matcher(input).find() ||
                PASSWORD_PATTERN.matcher(input).find();
    }

    /**
     * Truncates content to a maximum length.
     *
     * @param content   the content to truncate
     * @param maxLength maximum allowed length
     * @return truncated content
     */
    public String truncate(String content, int maxLength) {
        if (content == null || content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }
}
