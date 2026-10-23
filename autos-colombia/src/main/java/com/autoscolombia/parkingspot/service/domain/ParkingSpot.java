package com.autoscolombia.parkingspot.service.domain;

import com.autoscolombia.common.service.domain.Audit;
import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ParkingSpot extends Audit {

    @Serial
    private static final long serialVersionUID = 2638391320735991185L;

    private Long id;

    @NotEmpty(message = "El numero de la celda no debe estar vacio.")
    @Size(max = 20, message = "El numero de la celda no debe ser mayor a {max} caracteres.")
    private String spotNumber;

    private boolean available;

    @NotNull(message = "El tipo del vehiculo no debe estar vacio.")
    private VehicleType vehicleType;

    public ParkingSpot switchToAvailable() {

        return this.toBuilder()
                .available(true)
                .build();
    }

    public ParkingSpot switchToUnavailable() {
        return this.toBuilder()
                .available(false)
                .build();
    }
}
