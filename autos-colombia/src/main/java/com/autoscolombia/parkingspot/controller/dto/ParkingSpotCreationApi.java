package com.autoscolombia.parkingspot.controller.dto;

import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleType;

import java.io.Serial;
import java.io.Serializable;

public record ParkingSpotCreationApi(
        Long id,
        String spotNumber,
        Boolean isAvailable,
        VehicleType vehicleType) implements Serializable {

    @Serial
    private static final long serialVersionUID = -2385889763627580207L;
}
