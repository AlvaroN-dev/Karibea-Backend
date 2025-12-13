package com.microservice.chatbot.infrastructure.adapters.retriever;

import com.microservice.chatbot.domain.models.ChatSource;
import com.microservice.chatbot.domain.port.out.RetrieverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL-based implementation of RetrieverService.
 * Location: infrastructure/adapters/retriever - Context retrieval adapter.
 * 
 * Note: This is a basic implementation. In production, consider using:
 * - Vector search with embeddings
 * - Full-text search with PostgreSQL
 * - Integration with external search services
 */
@Slf4j
@Component
public class SqlRetrieverService implements RetrieverService {

    @Override
    public List<ChatSource> retrieveContext(String query, int maxResults) {
        log.debug("Retrieving context for query: {} (max: {})", query, maxResults);

        List<ChatSource> sources = new ArrayList<>();

        // Basic keyword matching for demo purposes
        // In production, implement proper vector search or full-text search

        if (query == null || query.isEmpty()) {
            return sources;
        }

        String lowerQuery = query.toLowerCase();

        // Product-related context
        if (lowerQuery.contains("product") || lowerQuery.contains("producto") ||
                lowerQuery.contains("buy") || lowerQuery.contains("comprar")) {
            sources.add(ChatSource.builder()
                    .id("product-info-1")
                    .type(ChatSource.SourceType.PRODUCT)
                    .content("Our catalog includes a wide variety of products. " +
                            "Browse categories or search by name.")
                    .score(BigDecimal.valueOf(0.9))
                    .build());
        }

        // Order-related context
        if (lowerQuery.contains("order") || lowerQuery.contains("pedido") ||
                lowerQuery.contains("track") || lowerQuery.contains("tracking")) {
            sources.add(ChatSource.builder()
                    .id("order-faq-1")
                    .type(ChatSource.SourceType.FAQ)
                    .content("To track your order, use the order number from your confirmation email. " +
                            "Orders typically arrive within 3-5 business days.")
                    .score(BigDecimal.valueOf(0.85))
                    .build());
        }

        // Shipping context
        if (lowerQuery.contains("shipping") || lowerQuery.contains("delivery") ||
                lowerQuery.contains("envío") || lowerQuery.contains("entrega")) {
            sources.add(ChatSource.builder()
                    .id("shipping-policy-1")
                    .type(ChatSource.SourceType.POLICY)
                    .content("Free shipping on orders over $50. Standard delivery 3-5 days, " +
                            "Express delivery 1-2 days available for additional fee.")
                    .score(BigDecimal.valueOf(0.88))
                    .build());
        }

        // Returns context
        if (lowerQuery.contains("return") || lowerQuery.contains("refund") ||
                lowerQuery.contains("devolución") || lowerQuery.contains("reembolso")) {
            sources.add(ChatSource.builder()
                    .id("return-policy-1")
                    .type(ChatSource.SourceType.POLICY)
                    .content("30-day return policy. Items must be unused and in original packaging. " +
                            "Refunds processed within 5-7 business days.")
                    .score(BigDecimal.valueOf(0.92))
                    .build());
        }

        // Limit results
        if (sources.size() > maxResults) {
            sources = sources.subList(0, maxResults);
        }

        log.debug("Found {} context sources for query", sources.size());
        return sources;
    }

    @Override
    public List<ChatSource> getAllSources() {
        return List.of(
                ChatSource.builder()
                        .id("source-type-product")
                        .type(ChatSource.SourceType.PRODUCT)
                        .content("Product catalog data")
                        .score(BigDecimal.ONE)
                        .build(),
                ChatSource.builder()
                        .id("source-type-order")
                        .type(ChatSource.SourceType.ORDER)
                        .content("Order information")
                        .score(BigDecimal.ONE)
                        .build(),
                ChatSource.builder()
                        .id("source-type-faq")
                        .type(ChatSource.SourceType.FAQ)
                        .content("Frequently asked questions")
                        .score(BigDecimal.ONE)
                        .build(),
                ChatSource.builder()
                        .id("source-type-policy")
                        .type(ChatSource.SourceType.POLICY)
                        .content("Store policies")
                        .score(BigDecimal.ONE)
                        .build());
    }

    @Override
    public List<ChatSource> retrieveByType(ChatSource.SourceType type, int maxResults) {
        log.debug("Retrieving sources by type: {} (max: {})", type, maxResults);

        return getAllSources().stream()
                .filter(source -> source.getType() == type)
                .limit(maxResults)
                .toList();
    }
}
