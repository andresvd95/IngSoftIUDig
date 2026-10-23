package com.autoscolombia.operator.controller.dto;

import java.io.Serial;
import java.io.Serializable;

public record OperatorSummaryViewApi(
        Long id,
        String name
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 3489147184713003264L;
}