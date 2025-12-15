package com.microservice.marketing.domain.event;

import java.util.UUID;

public class PromotionCreatedEvent extends DomainEvent {
    private final UUID promotionId;
    private final String name;

    public PromotionCreatedEvent(UUID promotionId, String name) {
        super("PromotionCreatedEvent");
        this.promotionId = promotionId;
        this.name = name;
    }

    public UUID getPromotionId() {
        return promotionId;
    }

    public String getName() {
        return name;
    }
}
