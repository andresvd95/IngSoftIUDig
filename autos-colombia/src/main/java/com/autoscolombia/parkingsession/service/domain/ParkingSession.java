package com.autoscolombia.parkingsession.service.domain;

import com.autoscolombia.common.service.domain.Audit;
import com.autoscolombia.operator.service.domain.Operator;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.vehicle.service.domain.Vehicle;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ParkingSession extends Audit {

    @Serial
    private static final long serialVersionUID = -8001896409710732957L;

    private Long id;

    @NotNull(message = "La fecha y hora de entrada son obligatorias.")
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    @NotNull(message = "El vehículo es obligatorio.")
    private Vehicle vehicle;

    @NotNull(message = "La celda de parqueo es obligatoria.")
    private ParkingSpot spot;

    @NotNull(message = "El operador de entrada es obligatorio.")
    private Operator entryOperator;

    private Operator exitOperator;

    public ParkingSession registerEntry() {
        return this.toBuilder()
                .entryTime(LocalDateTime.now())
                .build();
    }

    public ParkingSession registerExit() {
        return this.toBuilder()
                .exitTime(LocalDateTime.now())
                .build();
    }

    public Long getVehicleId() {
        return Optional.ofNullable(vehicle)
                .map(Vehicle::getId)
                .orElse(null);
    }

    public Long getSpotId() {
        return Optional.ofNullable(spot)
                .map(ParkingSpot::getId)
                .orElse(null);
    }

    public Long getExitOperatorId() {
        return Optional.ofNullable(exitOperator)
                .map(Operator::getId)
                .orElse(null);
    }
}
