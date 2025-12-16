package com.microservice.search.application.usecases;

import com.microservice.search.domain.models.SearchQuery;
import com.microservice.search.domain.models.SearchResult;
import com.microservice.search.domain.port.in.SearchProductsUseCase;
import com.microservice.search.domain.port.out.SearchEnginePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementación del caso de uso de búsqueda de productos.
 */
@Service
public class SearchProductsUseCaseImpl implements SearchProductsUseCase {

    private static final Logger log = LoggerFactory.getLogger(SearchProductsUseCaseImpl.class);

    private final SearchEnginePort searchEnginePort;

    public SearchProductsUseCaseImpl(SearchEnginePort searchEnginePort) {
        this.searchEnginePort = searchEnginePort;
    }

    @Override
    public SearchResult execute(SearchQuery query) {
        log.info("Executing search for term: '{}', page: {}, size: {}",
                query.term(), query.page(), query.size());

        long startTime = System.currentTimeMillis();

        SearchResult result = searchEnginePort.search(query);

        long duration = System.currentTimeMillis() - startTime;
        log.info("Search completed. Found {} results in {}ms", result.totalHits(), duration);

        return result;
    }
}
