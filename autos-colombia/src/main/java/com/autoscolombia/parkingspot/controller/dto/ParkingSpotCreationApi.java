package com.autoscolombia.parkingspot.controller.dto;

import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public record ParkingSpotCreationApi(
        Long id,

        @NotBlank(message = "El número de la celda es obligatorio.")
        @Size(max = 20, message = "El número de la celda no debe ser mayor a {max} caracteres.")
        String spotNumber,

        Boolean isAvailable,

        @NotNull(message = "El tipo de vehículo es obligatorio.")
        VehicleType vehicleType) implements Serializable {

    @Serial
    private static final long serialVersionUID = -2385889763627580207L;
}
