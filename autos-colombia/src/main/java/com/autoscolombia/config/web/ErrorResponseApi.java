package com.autoscolombia.config.web;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public record ErrorResponseApi(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path) implements Serializable {

    @Serial
    private static final long serialVersionUID = -3576657458686487567L;
}