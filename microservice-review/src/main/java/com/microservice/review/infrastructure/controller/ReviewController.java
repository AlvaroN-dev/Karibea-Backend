package com.microservice.review.infrastructure.controller;

import com.microservice.review.application.dto.*;
import com.microservice.review.application.mapper.ReviewMapper;
import com.microservice.review.domain.models.ProductReview;
import com.microservice.review.domain.models.ReviewStatus;
import com.microservice.review.domain.models.VoteType;
import com.microservice.review.domain.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Review operations.
 * Follows Hexagonal Architecture - this is a driving adapter.
 */
@RestController
@RequestMapping("/api/v1/reviews")
@Tag(name = "Reviews", description = "Product review management API")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    private final CreateReviewUseCase createReviewUseCase;
    private final ModerateReviewUseCase moderateReviewUseCase;
    private final VoteReviewUseCase voteReviewUseCase;
    private final DeleteReviewUseCase deleteReviewUseCase;
    private final GetReviewDetailUseCase getReviewDetailUseCase;
    private final ListReviewsByProductUseCase listReviewsByProductUseCase;
    private final ReportReviewUseCase reportReviewUseCase;
    private final ReviewMapper reviewMapper;

    public ReviewController(CreateReviewUseCase createReviewUseCase,
            ModerateReviewUseCase moderateReviewUseCase,
            VoteReviewUseCase voteReviewUseCase,
            DeleteReviewUseCase deleteReviewUseCase,
            GetReviewDetailUseCase getReviewDetailUseCase,
            ListReviewsByProductUseCase listReviewsByProductUseCase,
            ReportReviewUseCase reportReviewUseCase,
            ReviewMapper reviewMapper) {
        this.createReviewUseCase = createReviewUseCase;
        this.moderateReviewUseCase = moderateReviewUseCase;
        this.voteReviewUseCase = voteReviewUseCase;
        this.deleteReviewUseCase = deleteReviewUseCase;
        this.getReviewDetailUseCase = getReviewDetailUseCase;
        this.listReviewsByProductUseCase = listReviewsByProductUseCase;
        this.reportReviewUseCase = reportReviewUseCase;
        this.reviewMapper = reviewMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new review", description = "Submits a new product review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully", content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "User has already reviewed this product")
    })
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request) {
        log.info("Received request to create review for product: {}", request.getProductId());

        CreateReviewUseCase.CreateReviewCommand command = new CreateReviewUseCase.CreateReviewCommand(
                request.getProductId(),
                request.getUserId(),
                request.getOrderId(),
                request.getOrderItemId(),
                request.getRating(),
                request.getTitle(),
                request.getComment(),
                request.getImageUrls(),
                request.isVerifiedPurchase());

        ProductReview review = createReviewUseCase.execute(command);
        ReviewResponse response = reviewMapper.toResponse(review);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get review by ID", description = "Retrieves detailed review information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found", content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ReviewResponse> getReview(
            @Parameter(description = "Review ID") @PathVariable UUID id) {
        log.debug("Received request to get review: {}", id);

        ProductReview review = getReviewDetailUseCase.execute(id);
        ReviewResponse response = reviewMapper.toResponse(review);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "List reviews by product", description = "Retrieves paginated reviews for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully", content = @Content(schema = @Schema(implementation = ReviewListResponse.class)))
    })
    public ResponseEntity<ReviewListResponse> getReviewsByProduct(
            @Parameter(description = "Product ID") @PathVariable UUID productId,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        log.debug("Received request to list reviews for product: {}", productId);

        ListReviewsByProductUseCase.ListReviewsQuery query = new ListReviewsByProductUseCase.ListReviewsQuery(productId,
                page, size);

        ListReviewsByProductUseCase.ListReviewsResult result = listReviewsByProductUseCase.execute(query);

        ReviewListResponse response = new ReviewListResponse(
                reviewMapper.toResponseList(result.reviews()),
                result.totalElements(),
                result.totalPages(),
                result.currentPage(),
                size,
                result.averageRating());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/moderate")
    @Operation(summary = "Moderate a review", description = "Approves, rejects, or flags a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review moderated successfully", content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found"),
            @ApiResponse(responseCode = "400", description = "Invalid moderation action")
    })
    public ResponseEntity<ReviewResponse> moderateReview(
            @Parameter(description = "Review ID") @PathVariable UUID id,
            @Valid @RequestBody ModerateReviewRequest request) {
        log.info("Received request to moderate review: {} with action: {}", id, request.getAction());

        ModerateReviewUseCase.ModerateReviewCommand command = new ModerateReviewUseCase.ModerateReviewCommand(
                id,
                request.getModeratorId(),
                ReviewStatus.valueOf(request.getAction().toUpperCase()),
                request.getReason());

        ProductReview review = moderateReviewUseCase.execute(command);
        ReviewResponse response = reviewMapper.toResponse(review);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/vote")
    @Operation(summary = "Vote on a review", description = "Marks a review as helpful or not helpful")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vote recorded successfully", content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found"),
            @ApiResponse(responseCode = "409", description = "User has already voted on this review")
    })
    public ResponseEntity<ReviewResponse> voteReview(
            @Parameter(description = "Review ID") @PathVariable UUID id,
            @Valid @RequestBody VoteReviewRequest request) {
        log.info("Received request to vote on review: {} with type: {}", id, request.getVoteType());

        VoteReviewUseCase.VoteReviewCommand command = new VoteReviewUseCase.VoteReviewCommand(
                id,
                request.getUserId(),
                VoteType.valueOf(request.getVoteType().toUpperCase()));

        ProductReview review = voteReviewUseCase.execute(command);
        ReviewResponse response = reviewMapper.toResponse(review);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/report")
    @Operation(summary = "Report a review", description = "Reports a review as inappropriate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review reported successfully", content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ReviewResponse> reportReview(
            @Parameter(description = "Review ID") @PathVariable UUID id) {
        log.info("Received request to report review: {}", id);

        ProductReview review = reportReviewUseCase.execute(id);
        ReviewResponse response = reviewMapper.toResponse(review);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review", description = "Soft deletes a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted successfully", content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ReviewResponse> deleteReview(
            @Parameter(description = "Review ID") @PathVariable UUID id) {
        log.info("Received request to delete review: {}", id);

        ProductReview review = deleteReviewUseCase.execute(id);
        ReviewResponse response = reviewMapper.toResponse(review);

        return ResponseEntity.ok(response);
    }
}
