package com.microservice.review.application.usecases;

import com.microservice.review.domain.exceptions.ReviewNotFoundException;
import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.port.in.ReportReviewUseCase;
import com.microservice.review.domain.port.out.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of ReportReviewUseCase.
 */
@Service
public class ReportReviewUseCaseImpl implements ReportReviewUseCase {

    private static final Logger log = LoggerFactory.getLogger(ReportReviewUseCaseImpl.class);

    private final ReviewRepository reviewRepository;

    public ReportReviewUseCaseImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public ProductReview execute(UUID reviewId) {
        log.info("Reporting review: {}", reviewId);

        ProductReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        review.report();

        ProductReview savedReview = reviewRepository.save(review);

        log.info("Review reported successfully: {}, total reports: {}",
                reviewId, savedReview.getReportedCount());
        return savedReview;
    }
}
