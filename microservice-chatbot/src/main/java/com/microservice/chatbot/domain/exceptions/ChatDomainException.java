package com.microservice.chatbot.domain.exceptions;

/**
 * Base exception for all domain-level exceptions.
 * Location: domain/exceptions - No Spring dependencies.
 */
public class ChatDomainException extends RuntimeException {

    public ChatDomainException(String message) {
        super(message);
    }

    public ChatDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
