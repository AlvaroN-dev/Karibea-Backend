package com.microservice.search.domain.exceptions;

/**
 * Excepción para queries de búsqueda inválidos.
 */
public class InvalidSearchQueryException extends DomainException {

    public InvalidSearchQueryException(String message) {
        super("INVALID_SEARCH_QUERY", message);
    }
}
