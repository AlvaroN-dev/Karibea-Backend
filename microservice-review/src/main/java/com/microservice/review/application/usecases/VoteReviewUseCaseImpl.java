package com.microservice.review.application.usecases;

import com.microservice.review.domain.events.ReviewVotedEvent;
import com.microservice.review.domain.exceptions.ReviewNotFoundException;
import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.port.in.VoteReviewUseCase;
import com.microservice.review.domain.port.out.ReviewEventPublisher;
import com.microservice.review.domain.port.out.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of VoteReviewUseCase.
 */
@Service
public class VoteReviewUseCaseImpl implements VoteReviewUseCase {

    private static final Logger log = LoggerFactory.getLogger(VoteReviewUseCaseImpl.class);

    private final ReviewRepository reviewRepository;
    private final ReviewEventPublisher eventPublisher;

    public VoteReviewUseCaseImpl(ReviewRepository reviewRepository,
            ReviewEventPublisher eventPublisher) {
        this.reviewRepository = reviewRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ProductReview execute(VoteReviewCommand command) {
        log.info("User {} voting {} on review {}",
                command.userId(), command.voteType(), command.reviewId());

        ProductReview review = reviewRepository.findById(command.reviewId())
                .orElseThrow(() -> new ReviewNotFoundException(command.reviewId()));

        // Add vote (domain model enforces one vote per user invariant)
        review.addVote(command.userId(), command.voteType());

        // Save the updated review
        ProductReview savedReview = reviewRepository.save(review);

        // Publish domain event
        ReviewVotedEvent event = new ReviewVotedEvent(
                savedReview.getId(),
                command.userId(),
                command.voteType());
        eventPublisher.publish(event);

        log.info("Vote recorded successfully for review: {}", command.reviewId());
        return savedReview;
    }
}
