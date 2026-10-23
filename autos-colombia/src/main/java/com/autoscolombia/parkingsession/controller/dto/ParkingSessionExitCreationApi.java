package com.autoscolombia.parkingsession.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

public record ParkingSessionExitCreationApi(

        @NotNull(message = "El ID de la sesion es obligatorio")
        Long id,

        @NotNull(message = "El ID de la celda es obligatorio")
        Long spotId,

        @NotNull(message = "El ID del operador de salida es obligatorio")
        Long exitOperatorId) implements Serializable {

    @Serial
    private static final long serialVersionUID = -5896758051943772060L;
}