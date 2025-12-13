package com.microservice.review.application.mapper;

import com.microservice.review.application.dto.ReviewImageResponse;
import com.microservice.review.application.dto.ReviewResponse;
import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.models.ReviewImage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between domain models and DTOs.
 * Follows SRP - single responsibility for mapping.
 */
@Component
public class ReviewMapper {

    /**
     * Converts a ProductReview domain model to a ReviewResponse DTO.
     */
    public ReviewResponse toResponse(ProductReview review) {
        if (review == null) {
            return null;
        }

        return new ReviewResponse(
                review.getId(),
                review.getExternalProductId(),
                review.getExternalUserProfileId(),
                review.getExternalOrderId(),
                review.getRatingValue(),
                review.getTitle(),
                review.getComment(),
                review.getStatus().name(),
                review.getHelpfulVoteCount(),
                review.getUnhelpfulVoteCount(),
                review.getReportedCount(),
                review.isVerifiedPurchase(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                toImageResponseList(review.getImages()));
    }

    /**
     * Converts a list of ProductReview domain models to ReviewResponse DTOs.
     */
    public List<ReviewResponse> toResponseList(List<ProductReview> reviews) {
        if (reviews == null) {
            return List.of();
        }
        return reviews.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts a ReviewImage domain model to a ReviewImageResponse DTO.
     */
    public ReviewImageResponse toImageResponse(ReviewImage image) {
        if (image == null) {
            return null;
        }

        return new ReviewImageResponse(
                image.getId(),
                image.getUrl(),
                image.getDisplayOrder(),
                image.getCreatedAt());
    }

    /**
     * Converts a list of ReviewImage domain models to ReviewImageResponse DTOs.
     */
    public List<ReviewImageResponse> toImageResponseList(List<ReviewImage> images) {
        if (images == null) {
            return List.of();
        }
        return images.stream()
                .map(this::toImageResponse)
                .collect(Collectors.toList());
    }
}
