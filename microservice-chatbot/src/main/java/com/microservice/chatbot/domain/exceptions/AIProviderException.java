package com.microservice.chatbot.domain.exceptions;

/**
 * Exception thrown when AI provider fails to generate a response.
 * Location: domain/exceptions - No Spring dependencies.
 */
public class AIProviderException extends ChatDomainException {

    private final String providerName;

    public AIProviderException(String providerName, String message) {
        super(String.format("[%s] %s", providerName, message));
        this.providerName = providerName;
    }

    public AIProviderException(String providerName, String message, Throwable cause) {
        super(String.format("[%s] %s", providerName, message), cause);
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }
}
