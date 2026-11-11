package com.autoscolombia.customer.controller.dto;

import java.io.Serial;
import java.io.Serializable;

public record CustomerSummaryViewApi(
        Long id,
        String identificationNumber,
        String name) implements Serializable {

    @Serial
    private static final long serialVersionUID = 7189244019354120633L;
}
