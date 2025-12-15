package com.microservice.search.infrastructure.controller;

import com.microservice.search.application.dto.SearchRequestDTO;
import com.microservice.search.application.dto.SearchResponseDTO;
import com.microservice.search.application.mapper.ProductMapper;
import com.microservice.search.domain.models.SearchQuery;
import com.microservice.search.domain.models.SearchResult;
import com.microservice.search.domain.port.in.SearchProductsUseCase;
import com.microservice.search.domain.port.in.SyncProductsUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Controlador REST para operaciones de búsqueda.
 */
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    private final SearchProductsUseCase searchProductsUseCase;
    private final SyncProductsUseCase syncProductsUseCase;
    private final ProductMapper productMapper;

    public SearchController(
            SearchProductsUseCase searchProductsUseCase,
            SyncProductsUseCase syncProductsUseCase,
            ProductMapper productMapper) {
        this.searchProductsUseCase = searchProductsUseCase;
        this.syncProductsUseCase = syncProductsUseCase;
        this.productMapper = productMapper;
    }

    /**
     * Busca productos por término.
     */
    @GetMapping
    public ResponseEntity<SearchResponseDTO> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand) {

        log.info("Search request: query='{}', page={}, size={}", query, page, size);

        SearchRequestDTO request = new SearchRequestDTO(
                query, page, size, category, brand, null, null, null, null);

        SearchQuery searchQuery = productMapper.toDomain(request);
        SearchResult result = searchProductsUseCase.execute(searchQuery);
        SearchResponseDTO response = productMapper.toDto(result);

        return ResponseEntity.ok(response);
    }

    /**
     * Busca productos con body JSON.
     */
    @PostMapping
    public ResponseEntity<SearchResponseDTO> searchWithBody(@Valid @RequestBody SearchRequestDTO request) {
        log.info("Search request with body: query='{}'", request.query());

        SearchQuery searchQuery = productMapper.toDomain(request);
        SearchResult result = searchProductsUseCase.execute(searchQuery);
        SearchResponseDTO response = productMapper.toDto(result);

        return ResponseEntity.ok(response);
    }

    /**
     * Sincroniza todos los productos desde el catálogo.
     */
    @PostMapping("/sync")
    public Mono<ResponseEntity<SyncResponse>> syncAll() {
        log.info("Starting full sync");

        return syncProductsUseCase.syncAllProducts()
                .map(count -> ResponseEntity.ok(new SyncResponse(count, "Full sync completed")));
    }

    /**
     * Sincroniza productos de una tienda específica.
     */
    @PostMapping("/sync/store/{storeId}")
    public Mono<ResponseEntity<SyncResponse>> syncByStore(@PathVariable UUID storeId) {
        log.info("Starting sync for store: {}", storeId);

        return syncProductsUseCase.syncProductsByStore(storeId)
                .map(count -> ResponseEntity.ok(new SyncResponse(count, "Store sync completed")));
    }

    /**
     * Sincroniza un producto específico.
     */
    @PostMapping("/sync/product/{productId}")
    public Mono<ResponseEntity<SyncResponse>> syncProduct(@PathVariable UUID productId) {
        log.info("Syncing product: {}", productId);

        return syncProductsUseCase.syncProduct(productId)
                .map(success -> ResponseEntity.ok(
                        new SyncResponse(success ? 1 : 0, success ? "Product synced" : "Sync failed")));
    }

    private record SyncResponse(int count, String message) {
    }
}
