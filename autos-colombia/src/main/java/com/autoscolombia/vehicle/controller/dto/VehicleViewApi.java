package com.autoscolombia.vehicle.controller.dto;

import com.autoscolombia.customer.controller.dto.CustomerSummaryViewApi;
import com.autoscolombia.vehicle.subdomain.vehicletype.controller.dto.VehicleTypeViewApi;

import java.io.Serial;
import java.io.Serializable;

public record VehicleViewApi(
        Long id,
        String licensePlate,
        String brand,
        Integer model,
        String color,
        VehicleTypeViewApi vehicleType,
        CustomerSummaryViewApi customer) implements Serializable {

    @Serial
    private static final long serialVersionUID = 5124880213311769044L;
}
