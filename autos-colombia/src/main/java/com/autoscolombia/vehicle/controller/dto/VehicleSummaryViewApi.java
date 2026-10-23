package com.autoscolombia.vehicle.controller.dto;

import java.io.Serial;
import java.io.Serializable;

public record VehicleSummaryViewApi(
        Long id,
        String licensePlate) implements Serializable {

    @Serial
    private static final long serialVersionUID = -2501029284059253542L;
}