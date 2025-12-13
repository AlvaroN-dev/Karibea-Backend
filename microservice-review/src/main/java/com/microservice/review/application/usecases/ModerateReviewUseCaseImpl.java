package com.microservice.review.application.usecases;

import com.microservice.review.domain.events.ReviewModeratedEvent;
import com.microservice.review.domain.exceptions.ReviewNotFoundException;
import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.port.in.ModerateReviewUseCase;
import com.microservice.review.domain.port.out.ReviewEventPublisher;
import com.microservice.review.domain.port.out.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of ModerateReviewUseCase.
 */
@Service
public class ModerateReviewUseCaseImpl implements ModerateReviewUseCase {

    private static final Logger log = LoggerFactory.getLogger(ModerateReviewUseCaseImpl.class);

    private final ReviewRepository reviewRepository;
    private final ReviewEventPublisher eventPublisher;

    public ModerateReviewUseCaseImpl(ReviewRepository reviewRepository,
            ReviewEventPublisher eventPublisher) {
        this.reviewRepository = reviewRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ProductReview execute(ModerateReviewCommand command) {
        log.info("Moderating review: {} with action: {}", command.reviewId(), command.action());

        ProductReview review = reviewRepository.findById(command.reviewId())
                .orElseThrow(() -> new ReviewNotFoundException(command.reviewId()));

        // Apply moderation action
        switch (command.action()) {
            case APPROVED -> review.approve(command.moderatorId());
            case REJECTED -> review.reject(command.moderatorId(), command.reason());
            case FLAGGED -> review.flag(command.moderatorId(), command.reason());
            default -> throw new IllegalArgumentException("Invalid moderation action: " + command.action());
        }

        // Save the updated review
        ProductReview savedReview = reviewRepository.save(review);

        // Publish domain event
        ReviewModeratedEvent event = new ReviewModeratedEvent(
                savedReview.getId(),
                savedReview.getExternalProductId(),
                command.moderatorId(),
                command.action(),
                command.reason());
        eventPublisher.publish(event);

        log.info("Review moderated successfully: {} -> {}", command.reviewId(), command.action());
        return savedReview;
    }
}
