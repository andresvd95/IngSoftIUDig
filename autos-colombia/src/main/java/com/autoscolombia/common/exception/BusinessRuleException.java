package com.autoscolombia.common.exception;

/**
 * Se lanza cuando una operación viola una regla de negocio (HTTP 409).
 * Ej.: vehículo ya dentro, celda ocupada, sesión ya cerrada.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
