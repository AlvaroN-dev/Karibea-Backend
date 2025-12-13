package com.microservice.chatbot.domain.exceptions;

/**
 * Exception thrown when message limit is exceeded in a conversation.
 * Location: domain/exceptions - No Spring dependencies.
 */
public class MessageLimitExceededException extends ChatDomainException {

    private final int currentCount;
    private final int maxAllowed;

    public MessageLimitExceededException(int currentCount, int maxAllowed) {
        super(String.format("Message limit exceeded. Current: %d, Maximum allowed: %d",
                currentCount, maxAllowed));
        this.currentCount = currentCount;
        this.maxAllowed = maxAllowed;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public int getMaxAllowed() {
        return maxAllowed;
    }
}
