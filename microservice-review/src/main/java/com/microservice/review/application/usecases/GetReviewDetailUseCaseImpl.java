package com.microservice.review.application.usecases;

import com.microservice.review.domain.exceptions.ReviewNotFoundException;
import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.port.in.GetReviewDetailUseCase;
import com.microservice.review.domain.port.out.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of GetReviewDetailUseCase.
 */
@Service
public class GetReviewDetailUseCaseImpl implements GetReviewDetailUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetReviewDetailUseCaseImpl.class);

    private final ReviewRepository reviewRepository;

    public GetReviewDetailUseCaseImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductReview execute(UUID reviewId) {
        log.debug("Getting review details for: {}", reviewId);

        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }
}
