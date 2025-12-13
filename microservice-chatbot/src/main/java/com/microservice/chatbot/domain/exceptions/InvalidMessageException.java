package com.microservice.chatbot.domain.exceptions;

/**
 * Exception thrown when a message is invalid.
 * Location: domain/exceptions - No Spring dependencies.
 */
public class InvalidMessageException extends ChatDomainException {

    public InvalidMessageException(String message) {
        super(message);
    }

    public static InvalidMessageException empty() {
        return new InvalidMessageException("Message content cannot be empty");
    }

    public static InvalidMessageException tooLong(int maxLength) {
        return new InvalidMessageException(
                String.format("Message content exceeds maximum length of %d characters", maxLength));
    }
}
