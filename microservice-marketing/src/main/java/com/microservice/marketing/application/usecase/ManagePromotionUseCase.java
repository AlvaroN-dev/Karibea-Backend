package com.microservice.marketing.application.usecase;

import com.microservice.marketing.domain.model.Promotion;
import com.microservice.marketing.domain.port.PromotionRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagePromotionUseCase {

    private final PromotionRepositoryPort promotionRepository;
    private final com.microservice.marketing.infrastructure.kafka.KafkaEventProducer kafkaEventProducer;

    public ManagePromotionUseCase(PromotionRepositoryPort promotionRepository,
            com.microservice.marketing.infrastructure.kafka.KafkaEventProducer kafkaEventProducer) {
        this.promotionRepository = promotionRepository;
        this.kafkaEventProducer = kafkaEventProducer;
    }

    @Transactional
    public Promotion createPromotion(Promotion promotion) {
        Promotion saved = promotionRepository.save(promotion);
        kafkaEventProducer.publish("marketing.promotion.created",
                new com.microservice.marketing.domain.event.PromotionCreatedEvent(saved.getId(), saved.getName()));
        return saved;
    }

    @Transactional
    public Promotion updatePromotion(UUID id, Promotion promotion) {
        if (promotionRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Promotion not found with id: " + id);
        }
        promotion.setId(id);
        return promotionRepository.save(promotion);
    }

    @Transactional(readOnly = true)
    public Promotion getPromotion(UUID id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    @Transactional
    public void deletePromotion(UUID id) {
        promotionRepository.deleteById(id);
    }
}
