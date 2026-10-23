package com.autoscolombia.parkingsession.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

public record ParkingSessionIngressCreationApi(

        @NotNull(message = "El ID del vehículo es obligatorio")
        Long vehicleId,

        @NotNull(message = "El ID de la celda es obligatorio")
        Long spotId,

        @NotNull(message = "El ID del operador de entrada es obligatorio")
        Long entryOperatorId) implements Serializable {

    @Serial
    private static final long serialVersionUID = 3657115979042546901L;
}