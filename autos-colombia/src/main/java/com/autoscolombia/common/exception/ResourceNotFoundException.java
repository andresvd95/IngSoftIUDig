package com.autoscolombia.common.exception;

/**
 * Se lanza cuando un recurso referenciado no existe (HTTP 404).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
