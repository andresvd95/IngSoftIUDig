package com.autoscolombia.vehicle.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public record VehicleCreationApi(
        @NotBlank(message = "La placa es obligatoria.")
        @Size(max = 20, message = "La placa no debe exceder los {max} caracteres.")
        String licensePlate,

        @Size(max = 50, message = "La marca no debe exceder los {max} caracteres.")
        String brand,

        @Min(value = 1900, message = "El modelo debe ser un año válido.")
        @Max(value = 2100, message = "El modelo debe ser un año válido.")
        Integer model,

        @Size(max = 50, message = "El color no debe exceder los {max} caracteres.")
        String color,

        @NotNull(message = "El cliente asociado es obligatorio.")
        Long customerId,

        @NotNull(message = "El tipo de vehículo es obligatorio.")
        Long vehicleTypeId) implements Serializable {

    @Serial
    private static final long serialVersionUID = 4120948840512377365L;
}
