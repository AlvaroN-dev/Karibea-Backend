package com.microservice.search.infrastructure.adapters;

import com.microservice.search.domain.models.ProductId;
import com.microservice.search.domain.models.SearchQuery;
import com.microservice.search.domain.models.SearchResult;
import com.microservice.search.domain.models.SearchableProduct;
import com.microservice.search.domain.port.out.SearchEnginePort;
import com.microservice.search.domain.exceptions.SearchEngineException;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResultPaginated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * Adaptador de Meilisearch para operaciones de búsqueda.
 */
@Component
public class MeilisearchAdapter implements SearchEnginePort {

    private static final Logger log = LoggerFactory.getLogger(MeilisearchAdapter.class);
    private static final String INDEX_NAME = "products";

    private final Client meilisearchClient;

    public MeilisearchAdapter(Client meilisearchClient) {
        this.meilisearchClient = meilisearchClient;
    }

    @Override
    public SearchResult search(SearchQuery query) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);

            SearchRequest request = new SearchRequest(query.term())
                    .setLimit(query.size())
                    .setOffset(query.offset());

            if (query.category() != null && !query.category().isBlank()) {
                request.setFilter(new String[]{"category = \"" + query.category() + "\""});
            }

            long startTime = System.currentTimeMillis();
            SearchResultPaginated result = (SearchResultPaginated) index.search(request);
            long processingTime = System.currentTimeMillis() - startTime;

            List<SearchableProduct> products = mapToProducts(result.getHits());

            return new SearchResult(
                    products,
                    result.getTotalHits(),
                    query.page(),
                    query.size(),
                    processingTime);

        } catch (Exception e) {
            log.error("Error searching in Meilisearch: {}", e.getMessage(), e);
            throw new SearchEngineException("Search operation failed", e);
        }
    }

    @Override
    public void index(SearchableProduct product) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            Map<String, Object> document = mapToDocument(product);
            String json = toJson(document);
            index.addDocuments("[" + json + "]");
            log.debug("Product indexed: {}", product.id());
        } catch (Exception e) {
            log.error("Error indexing product: {}", e.getMessage(), e);
            throw new SearchEngineException("Index operation failed", e);
        }
    }

    @Override
    public void indexBatch(List<SearchableProduct> products) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < products.size(); i++) {
                if (i > 0) json.append(",");
                json.append(toJson(mapToDocument(products.get(i))));
            }
            json.append("]");
            index.addDocuments(json.toString());
            log.info("Batch indexed {} products", products.size());
        } catch (Exception e) {
            log.error("Error batch indexing: {}", e.getMessage(), e);
            throw new SearchEngineException("Batch index operation failed", e);
        }
    }

    @Override
    public void delete(ProductId productId) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            index.deleteDocument(productId.value().toString());
            log.debug("Product deleted from index: {}", productId);
        } catch (Exception e) {
            log.error("Error deleting product: {}", e.getMessage(), e);
            throw new SearchEngineException("Delete operation failed", e);
        }
    }

    @Override
    public Optional<SearchableProduct> findById(ProductId productId) {
        try {
            Index index = meilisearchClient.index(INDEX_NAME);
            @SuppressWarnings("unchecked")
            Map<String, Object> document = index.getDocument(productId.value().toString(), Map.class);
            if (document == null) {
                return Optional.empty();
            }
            return Optional.of(mapToProduct(document));
        } catch (Exception e) {
            log.debug("Product not found: {}", productId);
            return Optional.empty();
        }
    }

    @Override
    public boolean exists(ProductId productId) {
        return findById(productId).isPresent();
    }

    private Map<String, Object> mapToDocument(SearchableProduct product) {
        Map<String, Object> doc = new LinkedHashMap<>();
        doc.put("id", product.id().value().toString());
        doc.put("storeId", product.storeId() != null ? product.storeId().toString() : null);
        doc.put("name", product.name());
        doc.put("description", product.description());
        doc.put("brand", product.brand());
        doc.put("categoryNames", product.categoryNames());
        doc.put("price", product.price());
        doc.put("currency", product.currency());
        doc.put("averageRating", product.averageRating());
        doc.put("reviewCount", product.reviewCount());
        doc.put("salesCount", product.salesCount());
        doc.put("isAvailable", product.isAvailable());
        doc.put("primaryImageUrl", product.primaryImageUrl());
        doc.put("indexedAt", product.indexedAt().toEpochMilli());
        return doc;
    }

    private List<SearchableProduct> mapToProducts(ArrayList<HashMap<String, Object>> hits) {
        return hits.stream()
                .map(this::mapToProduct)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private SearchableProduct mapToProduct(Map<String, Object> doc) {
        return new SearchableProduct(
                new ProductId(UUID.fromString((String) doc.get("id"))),
                doc.get("storeId") != null ? UUID.fromString((String) doc.get("storeId")) : null,
                (String) doc.get("name"),
                (String) doc.get("description"),
                (String) doc.get("brand"),
                (List<String>) doc.getOrDefault("categoryNames", List.of()),
                List.of(),
                doc.get("price") != null ? new BigDecimal(doc.get("price").toString()) : null,
                null,
                (String) doc.get("currency"),
                List.of(),
                List.of(),
                doc.get("averageRating") != null ? new BigDecimal(doc.get("averageRating").toString()) : null,
                doc.get("reviewCount") != null ? ((Number) doc.get("reviewCount")).intValue() : 0,
                doc.get("salesCount") != null ? ((Number) doc.get("salesCount")).intValue() : 0,
                0,
                Boolean.TRUE.equals(doc.get("isAvailable")),
                0,
                (String) doc.get("primaryImageUrl"),
                List.of(),
                true,
                Instant.ofEpochMilli(((Number) doc.get("indexedAt")).longValue()),
                null,
                false);
    }

    private String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append("\"").append(entry.getKey()).append("\":");
            Object v = entry.getValue();
            if (v == null) sb.append("null");
            else if (v instanceof String) sb.append("\"").append(v).append("\"");
            else if (v instanceof Number || v instanceof Boolean) sb.append(v);
            else if (v instanceof List<?> list) {
                sb.append("[");
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append("\"").append(list.get(i)).append("\"");
                }
                sb.append("]");
            } else sb.append("\"").append(v).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }
}
