package com.microservice.notification.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationCreatedEvent implements DomainEvent {

    private final UUID eventId;
    private final UUID aggregateId;
    private final LocalDateTime occurredAt;
    private final UUID notificationId;
    private final String externalUserProfileId;
    private final String channel;
    private final String title;
    private final String message;

    public NotificationCreatedEvent(UUID notificationId, String externalUserProfileId,
            String channel, String title, String message) {
        this.eventId = UUID.randomUUID();
        this.aggregateId = notificationId != null ? notificationId : UUID.randomUUID();
        this.occurredAt = LocalDateTime.now();
        this.notificationId = notificationId;
        this.externalUserProfileId = externalUserProfileId;
        this.channel = channel;
        this.title = title;
        this.message = message;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public String getEventType() {
        return "NotificationCreated";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public String getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public String getChannel() {
        return channel;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
