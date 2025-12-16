package com.microservice.search.application.mapper;

import com.microservice.search.application.dto.CatalogProductDTO;
import com.microservice.search.application.dto.ProductDTO;
import com.microservice.search.application.dto.SearchRequestDTO;
import com.microservice.search.application.dto.SearchResponseDTO;
import com.microservice.search.domain.models.ProductId;
import com.microservice.search.domain.models.SearchQuery;
import com.microservice.search.domain.models.SearchResult;
import com.microservice.search.domain.models.SearchableProduct;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/**
 * Mapper manual para conversión entre DTOs y modelos de dominio.
 */
@Component
public class ProductMapper {

    /**
     * Convierte SearchRequestDTO a SearchQuery (dominio).
     */
    public SearchQuery toDomain(SearchRequestDTO dto) {
        return new SearchQuery(
                dto.query(),
                dto.page(),
                dto.size(),
                dto.category(),
                dto.filters());
    }

    /**
     * Convierte SearchResult (dominio) a SearchResponseDTO.
     */
    public SearchResponseDTO toDto(SearchResult result) {
        List<ProductDTO> products = result.products().stream()
                .map(this::toProductDto)
                .toList();

        return SearchResponseDTO.of(
                products,
                result.totalHits(),
                result.page(),
                result.size(),
                result.processingTimeMs());
    }

    /**
     * Convierte SearchableProduct (dominio) a ProductDTO.
     */
    public ProductDTO toProductDto(SearchableProduct product) {
        return new ProductDTO(
                product.id().value(),
                product.storeId(),
                product.name(),
                product.description(),
                product.brand(),
                product.categoryNames(),
                product.price(),
                product.compareAtPrice(),
                product.currency(),
                product.averageRating(),
                product.reviewCount(),
                product.isAvailable(),
                product.primaryImageUrl(),
                product.imageUrls());
    }

    /**
     * Convierte CatalogProductDTO a SearchableProduct (dominio).
     */
    public SearchableProduct toDomain(CatalogProductDTO dto) {
        return new SearchableProduct(
                new ProductId(dto.id()),
                dto.storeId(),
                dto.name(),
                dto.description(),
                dto.brand(),
                dto.categoryNames(),
                dto.categoryIds(),
                dto.price(),
                dto.compareAtPrice(),
                dto.currency(),
                dto.colors(),
                dto.sizes(),
                dto.averageRating(),
                dto.reviewCount(),
                dto.salesCount(),
                dto.viewCount(),
                dto.isAvailable() != null && dto.isAvailable(),
                dto.stockQuantity(),
                dto.primaryImageUrl(),
                dto.imageUrls(),
                dto.isActive() != null && dto.isActive(),
                Instant.now(),
                null,
                false);
    }

    /**
     * Convierte lista de productos del catálogo a dominio.
     */
    public List<SearchableProduct> toDomainList(List<CatalogProductDTO> dtos) {
        return dtos.stream()
                .map(this::toDomain)
                .toList();
    }
}
