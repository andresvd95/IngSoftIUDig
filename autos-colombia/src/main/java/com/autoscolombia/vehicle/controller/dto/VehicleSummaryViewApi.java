package com.autoscolombia.vehicle.controller.dto;

import com.autoscolombia.customer.controller.dto.CustomerSummaryViewApi;

import java.io.Serial;
import java.io.Serializable;

public record VehicleSummaryViewApi(
        Long id,
        String licensePlate,
        String brand,
        Integer model,
        String color,
        CustomerSummaryViewApi customer) implements Serializable {

    @Serial
    private static final long serialVersionUID = -2501029284059253542L;
}
