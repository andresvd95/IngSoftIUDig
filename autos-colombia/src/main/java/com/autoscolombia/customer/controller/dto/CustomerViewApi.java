package com.autoscolombia.customer.controller.dto;

import java.io.Serial;
import java.io.Serializable;

public record CustomerViewApi(
        Long id,
        String identificationType,
        String identificationNumber,
        String name,
        String email,
        String phone) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6130953871240977105L;
}
