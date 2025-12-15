package com.microservice.search.domain.exceptions;

/**
 * Excepción cuando hay un error en el motor de búsqueda.
 */
public class SearchEngineException extends DomainException {

    public SearchEngineException(String message) {
        super("SEARCH_ENGINE_ERROR", message);
    }

    public SearchEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
