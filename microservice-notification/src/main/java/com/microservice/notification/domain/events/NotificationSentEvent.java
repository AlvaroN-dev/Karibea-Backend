package com.microservice.notification.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationSentEvent implements DomainEvent {

    private final UUID eventId;
    private final UUID aggregateId;
    private final LocalDateTime occurredAt;
    private final Long notificationId;
    private final String externalUserProfileId;
    private final String channel;

    public NotificationSentEvent(Long notificationId, String externalUserProfileId, String channel) {
        this.eventId = UUID.randomUUID();
        this.aggregateId = UUID.randomUUID();
        this.occurredAt = LocalDateTime.now();
        this.notificationId = notificationId;
        this.externalUserProfileId = externalUserProfileId;
        this.channel = channel;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public String getEventType() {
        return "NotificationSent";
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public String getExternalUserProfileId() {
        return externalUserProfileId;
    }

    public String getChannel() {
        return channel;
    }
}
