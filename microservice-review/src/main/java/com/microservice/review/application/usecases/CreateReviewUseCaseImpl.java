package com.microservice.review.application.usecases;

import com.microservice.review.domain.events.ReviewCreatedEvent;
import com.microservice.review.domain.exceptions.DuplicateReviewException;
import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.models.ReviewImage;
import com.microservice.review.domain.port.in.CreateReviewUseCase;
import com.microservice.review.domain.port.out.ReviewEventPublisher;
import com.microservice.review.domain.port.out.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CreateReviewUseCase.
 * Follows SRP - only handles review creation logic.
 */
@Service
public class CreateReviewUseCaseImpl implements CreateReviewUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateReviewUseCaseImpl.class);

    private final ReviewRepository reviewRepository;
    private final ReviewEventPublisher eventPublisher;

    public CreateReviewUseCaseImpl(ReviewRepository reviewRepository,
            ReviewEventPublisher eventPublisher) {
        this.reviewRepository = reviewRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ProductReview execute(CreateReviewCommand command) {
        log.info("Creating review for product: {} by user: {}",
                command.productId(), command.userId());

        // Check for duplicate review
        if (reviewRepository.existsByUserIdAndProductId(command.userId(), command.productId())) {
            throw new DuplicateReviewException(command.userId(), command.productId());
        }

        // Create the review
        ProductReview review = ProductReview.create(
                command.productId(),
                command.userId(),
                command.orderId(),
                command.orderItemId(),
                command.rating(),
                command.title(),
                command.comment(),
                command.verifiedPurchase());

        // Add images if provided
        if (command.imageUrls() != null && !command.imageUrls().isEmpty()) {
            int order = 0;
            for (String url : command.imageUrls()) {
                ReviewImage image = ReviewImage.create(review.getId(), url, order++);
                review.addImage(image);
            }
        }

        // Save the review
        ProductReview savedReview = reviewRepository.save(review);

        // Publish domain event
        ReviewCreatedEvent event = new ReviewCreatedEvent(
                savedReview.getId(),
                savedReview.getExternalProductId(),
                savedReview.getExternalUserProfileId(),
                savedReview.getRatingValue());
        eventPublisher.publish(event);

        log.info("Review created successfully with ID: {}", savedReview.getId());
        return savedReview;
    }
}
