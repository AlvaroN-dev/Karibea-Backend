package com.microservice.chatbot.application.exception;

/**
 * Application-level exception for chat operations.
 * Location: application/exception - Application layer exception.
 */
public class ChatApplicationException extends RuntimeException {

    public ChatApplicationException(String message) {
        super(message);
    }

    public ChatApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
