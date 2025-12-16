package com.microservice.review.application.usecases;

import com.microservice.review.domain.events.ReviewDeletedEvent;
import com.microservice.review.domain.exceptions.ReviewNotFoundException;
import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.port.in.DeleteReviewUseCase;
import com.microservice.review.domain.port.out.ReviewEventPublisher;
import com.microservice.review.domain.port.out.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of DeleteReviewUseCase.
 * Performs soft delete.
 */
@Service
public class DeleteReviewUseCaseImpl implements DeleteReviewUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteReviewUseCaseImpl.class);

    private final ReviewRepository reviewRepository;
    private final ReviewEventPublisher eventPublisher;

    public DeleteReviewUseCaseImpl(ReviewRepository reviewRepository,
            ReviewEventPublisher eventPublisher) {
        this.reviewRepository = reviewRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ProductReview execute(UUID reviewId) {
        log.info("Deleting review: {}", reviewId);

        ProductReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        // Soft delete
        review.softDelete();

        // Save the updated review
        ProductReview savedReview = reviewRepository.save(review);

        // Publish domain event
        ReviewDeletedEvent event = new ReviewDeletedEvent(
                savedReview.getId(),
                savedReview.getExternalProductId());
        eventPublisher.publish(event);

        log.info("Review deleted successfully: {}", reviewId);
        return savedReview;
    }
}
