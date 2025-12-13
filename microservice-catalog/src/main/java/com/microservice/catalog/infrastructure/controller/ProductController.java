package com.microservice.catalog.infrastructure.controller;

import com.microservice.catalog.application.dto.*;
import com.microservice.catalog.application.mapper.ProductMapper;
import com.microservice.catalog.domain.models.Product;
import com.microservice.catalog.domain.port.in.*;
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
 * REST Controller for Product operations.
 * Provides API endpoints for managing products.
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product management API")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final PublishProductUseCase publishProductUseCase;
    private final DeactivateProductUseCase deactivateProductUseCase;
    private final GetProductDetailUseCase getProductDetailUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final AddVariantUseCase addVariantUseCase;
    private final ProductMapper productMapper;

    public ProductController(CreateProductUseCase createProductUseCase,
            UpdateProductUseCase updateProductUseCase,
            PublishProductUseCase publishProductUseCase,
            DeactivateProductUseCase deactivateProductUseCase,
            GetProductDetailUseCase getProductDetailUseCase,
            ListProductsUseCase listProductsUseCase,
            AddVariantUseCase addVariantUseCase,
            ProductMapper productMapper) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.publishProductUseCase = publishProductUseCase;
        this.deactivateProductUseCase = deactivateProductUseCase;
        this.getProductDetailUseCase = getProductDetailUseCase;
        this.listProductsUseCase = listProductsUseCase;
        this.addVariantUseCase = addVariantUseCase;
        this.productMapper = productMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Product with same SKU already exists")
    })
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        log.info("Received request to create product: {}", request.getSku());

        CreateProductUseCase.CreateProductCommand command = new CreateProductUseCase.CreateProductCommand(
                request.getStoreId(),
                request.getSku(),
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                request.getBasePrice(),
                request.getCompareAtPrice(),
                request.getCurrency());

        Product product = createProductUseCase.execute(command);
        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves detailed product information by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "Product ID") @PathVariable UUID id) {
        log.debug("Received request to get product: {}", id);

        Product product = getProductDetailUseCase.execute(id);
        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List products", description = "Retrieves a paginated list of products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(schema = @Schema(implementation = ProductListResponse.class)))
    })
    public ResponseEntity<ProductListResponse> listProducts(
            @Parameter(description = "Store ID filter") @RequestParam(required = false) UUID storeId,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        log.debug("Received request to list products: storeId={}, page={}, size={}", storeId, page, size);

        ListProductsUseCase.ListProductsQuery query = new ListProductsUseCase.ListProductsQuery(storeId, page, size);

        ListProductsUseCase.ListProductsResult result = listProductsUseCase.execute(query);

        ProductListResponse response = new ProductListResponse(
                productMapper.toResponseList(result.products()),
                result.totalElements(),
                result.totalPages(),
                result.currentPage(),
                size);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Product ID") @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request) {
        log.info("Received request to update product: {}", id);

        UpdateProductUseCase.UpdateProductCommand command = new UpdateProductUseCase.UpdateProductCommand(
                id,
                request.getName(),
                request.getDescription(),
                request.getBrand(),
                request.getBasePrice(),
                request.getCompareAtPrice(),
                request.getCurrency(),
                request.getFeatured());

        Product product = updateProductUseCase.execute(command);
        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish product", description = "Publishes a product, making it visible to customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product published successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Cannot publish product without variants")
    })
    public ResponseEntity<ProductResponse> publishProduct(
            @Parameter(description = "Product ID") @PathVariable UUID id) {
        log.info("Received request to publish product: {}", id);

        Product product = publishProductUseCase.execute(id);
        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate product", description = "Deactivates a product, removing it from the storefront")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deactivated successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> deactivateProduct(
            @Parameter(description = "Product ID") @PathVariable UUID id) {
        log.info("Received request to deactivate product: {}", id);

        Product product = deactivateProductUseCase.execute(id);
        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/variants")
    @Operation(summary = "Add variant to product", description = "Adds a new variant to an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Variant added successfully", content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid variant data or duplicate SKU")
    })
    public ResponseEntity<ProductResponse> addVariant(
            @Parameter(description = "Product ID") @PathVariable UUID id,
            @Valid @RequestBody AddVariantRequest request) {
        log.info("Received request to add variant to product: {}", id);

        AddVariantUseCase.AddVariantCommand command = new AddVariantUseCase.AddVariantCommand(
                id,
                request.getSku(),
                request.getName(),
                request.getPrice(),
                request.getCompareAtPrice(),
                request.getBarcode());

        Product product = addVariantUseCase.execute(command);
        ProductResponse response = productMapper.toResponse(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
