package com.autoscolombia.vehicle.subdomain.vehicletype.controller.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public record VehicleTypeViewApi(
        Long id,
        String name,
        String description,
        BigDecimal monthlyRate) implements Serializable {

    @Serial
    private static final long serialVersionUID = 3598540886040751529L;
}