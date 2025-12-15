package com.microservice.review.infrastructure.adapters;

import com.microservice.review.domain.models.*;
import com.microservice.review.domain.port.out.ReviewRepository;
import com.microservice.review.infrastructure.entities.*;
import com.microservice.review.infrastructure.repositories.JpaProductReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing ReviewRepository port using Spring Data JPA.
 * Follows Hexagonal Architecture - infrastructure adapts to domain ports.
 */
@Component
public class ReviewRepositoryAdapter implements ReviewRepository {

    private final JpaProductReviewRepository jpaRepository;

    public ReviewRepositoryAdapter(JpaProductReviewRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ProductReview save(ProductReview review) {
        ProductReviewEntity entity = toEntity(review);
        ProductReviewEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<ProductReview> findById(UUID id) {
        return jpaRepository.findByIdWithDetails(id)
                .map(this::toDomain);
    }

    @Override
    public List<ProductReview> findByProductId(UUID productId, ReviewStatus status, int page, int size) {
        Page<ProductReviewEntity> pageResult = jpaRepository.findByExternalProductIdAndStatusAndDeletedFalse(
                productId,
                ReviewStatusEntity.valueOf(status.name()),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return pageResult.getContent().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductReview> findByUserId(UUID userId, int page, int size) {
        Page<ProductReviewEntity> pageResult = jpaRepository.findByExternalUserProfileIdAndDeletedFalse(
                userId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return pageResult.getContent().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUserIdAndProductId(UUID userId, UUID productId) {
        return jpaRepository.existsByExternalUserProfileIdAndExternalProductIdAndDeletedFalse(userId, productId);
    }

    @Override
    public long countByProductId(UUID productId, ReviewStatus status) {
        return jpaRepository.countByExternalProductIdAndStatusAndDeletedFalse(
                productId, ReviewStatusEntity.valueOf(status.name()));
    }

    @Override
    public long countByUserId(UUID userId) {
        return jpaRepository.countByExternalUserProfileIdAndDeletedFalse(userId);
    }

    @Override
    public Double getAverageRatingByProductId(UUID productId) {
        return jpaRepository.getAverageRatingByProductId(productId);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    // ========== Mapping Methods ==========

    private ProductReviewEntity toEntity(ProductReview review) {
        ProductReviewEntity entity = new ProductReviewEntity();
        entity.setId(review.getId());
        entity.setExternalProductId(review.getExternalProductId());
        entity.setExternalUserProfileId(review.getExternalUserProfileId());
        entity.setExternalOrderId(review.getExternalOrderId());
        entity.setExternalOrderItemId(review.getExternalOrderItemId());
        entity.setRating(review.getRatingValue());
        entity.setTitle(review.getTitle());
        entity.setComment(review.getComment());
        entity.setStatus(ReviewStatusEntity.valueOf(review.getStatus().name()));
        entity.setExternalModeratorId(review.getExternalModeratorId());
        entity.setModeratedAt(review.getModeratedAt());
        entity.setRejectionReason(review.getRejectionReason());
        entity.setHelpfulVoteCount(review.getHelpfulVoteCount());
        entity.setUnhelpfulVoteCount(review.getUnhelpfulVoteCount());
        entity.setReportedCount(review.getReportedCount());
        entity.setVerifiedPurchase(review.isVerifiedPurchase());
        entity.setCreatedAt(review.getCreatedAt());
        entity.setUpdatedAt(review.getUpdatedAt());
        entity.setDeletedAt(review.getDeletedAt());
        entity.setDeleted(review.isDeleted());

        // Map images
        for (ReviewImage image : review.getImages()) {
            ReviewImageEntity imageEntity = toImageEntity(image);
            entity.addImage(imageEntity);
        }

        // Map votes
        for (ReviewVote vote : review.getVotes()) {
            ReviewVoteEntity voteEntity = toVoteEntity(vote);
            entity.addVote(voteEntity);
        }

        return entity;
    }

    private ReviewImageEntity toImageEntity(ReviewImage image) {
        ReviewImageEntity entity = new ReviewImageEntity();
        entity.setId(image.getId());
        entity.setUrl(image.getUrl());
        entity.setDisplayOrder(image.getDisplayOrder());
        entity.setCreatedAt(image.getCreatedAt());
        return entity;
    }

    private ReviewVoteEntity toVoteEntity(ReviewVote vote) {
        ReviewVoteEntity entity = new ReviewVoteEntity();
        entity.setId(vote.getId());
        entity.setExternalUserProfileId(vote.getExternalUserProfileId());
        entity.setVoteType(vote.getVoteType().name());
        entity.setCreatedAt(vote.getCreatedAt());
        return entity;
    }

    private ProductReview toDomain(ProductReviewEntity entity) {
        ProductReview review = ProductReview.reconstitute(
                entity.getId(),
                entity.getExternalProductId(),
                entity.getExternalUserProfileId(),
                entity.getExternalOrderId(),
                entity.getExternalOrderItemId(),
                entity.getRating(),
                entity.getTitle(),
                entity.getComment(),
                ReviewStatus.valueOf(entity.getStatus().name()),
                entity.getExternalModeratorId(),
                entity.getModeratedAt(),
                entity.getRejectionReason(),
                entity.getHelpfulVoteCount(),
                entity.getUnhelpfulVoteCount(),
                entity.getReportedCount(),
                entity.getVerifiedPurchase(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt(),
                entity.getDeleted());

        // Map images
        for (ReviewImageEntity imageEntity : entity.getImages()) {
            ReviewImage image = toImageDomain(imageEntity);
            review.addImageForReconstitution(image);
        }

        // Map votes
        for (ReviewVoteEntity voteEntity : entity.getVotes()) {
            ReviewVote vote = toVoteDomain(voteEntity);
            review.addVoteForReconstitution(vote);
        }

        return review;
    }

    private ReviewImage toImageDomain(ReviewImageEntity entity) {
        return ReviewImage.reconstitute(
                entity.getId(),
                entity.getReview().getId(),
                entity.getUrl(),
                entity.getDisplayOrder(),
                entity.getCreatedAt());
    }

    private ReviewVote toVoteDomain(ReviewVoteEntity entity) {
        return ReviewVote.reconstitute(
                entity.getId(),
                entity.getReview().getId(),
                entity.getExternalUserProfileId(),
                VoteType.valueOf(entity.getVoteType()),
                entity.getCreatedAt());
    }
}
