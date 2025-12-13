package com.microservice.review.application.usecases;

import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.models.ReviewStatus;
import com.microservice.review.domain.port.in.ListReviewsByProductUseCase;
import com.microservice.review.domain.port.out.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of ListReviewsByProductUseCase.
 */
@Service
public class ListReviewsByProductUseCaseImpl implements ListReviewsByProductUseCase {

    private static final Logger log = LoggerFactory.getLogger(ListReviewsByProductUseCaseImpl.class);

    private final ReviewRepository reviewRepository;

    public ListReviewsByProductUseCaseImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ListReviewsResult execute(ListReviewsQuery query) {
        log.debug("Listing reviews for product: {}, page: {}, size: {}",
                query.productId(), query.page(), query.size());

        // Only show approved reviews to public
        List<ProductReview> reviews = reviewRepository.findByProductId(
                query.productId(),
                ReviewStatus.APPROVED,
                query.page(),
                query.size());

        long total = reviewRepository.countByProductId(query.productId(), ReviewStatus.APPROVED);
        int totalPages = (int) Math.ceil((double) total / query.size());
        Double averageRating = reviewRepository.getAverageRatingByProductId(query.productId());

        return new ListReviewsResult(
                reviews,
                total,
                totalPages,
                query.page(),
                averageRating);
    }
}
