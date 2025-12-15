package com.microservice.user.application.exception;

/**
 * Excepción base de la capa de aplicación
 */
public class ApplicationException extends RuntimeException {
    
    private final String code;
    
    public ApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    public ApplicationException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
}
