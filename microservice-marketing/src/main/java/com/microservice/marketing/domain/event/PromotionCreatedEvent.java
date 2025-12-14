package com.microservice.marketing.domain.event;

public class PromotionCreatedEvent extends DomainEvent {
    private final Long promotionId;
    private final String name;

    public PromotionCreatedEvent(Long promotionId, String name) {
        super("PromotionCreatedEvent");
        this.promotionId = promotionId;
        this.name = name;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public String getName() {
        return name;
    }
}
