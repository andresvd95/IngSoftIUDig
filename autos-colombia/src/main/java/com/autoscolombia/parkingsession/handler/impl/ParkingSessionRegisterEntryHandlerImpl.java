package com.autoscolombia.parkingsession.handler.impl;

import com.autoscolombia.common.exception.BusinessRuleException;
import com.autoscolombia.common.exception.ResourceNotFoundException;
import com.autoscolombia.operator.service.OperatorDomainService;
import com.autoscolombia.operator.service.domain.Operator;
import com.autoscolombia.parkingsession.handler.ParkingSessionRegisterEntryHandler;
import com.autoscolombia.parkingsession.service.ParkingSessionDomainService;
import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import com.autoscolombia.parkingspot.service.ParkingSpotDomainService;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.vehicle.service.VehicleDomainService;
import com.autoscolombia.vehicle.service.domain.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Transactional
public class ParkingSessionRegisterEntryHandlerImpl implements ParkingSessionRegisterEntryHandler {

    private final ParkingSessionDomainService parkingSessionDomainService;
    private final VehicleDomainService vehicleDomainService;
    private final ParkingSpotDomainService parkingSpotDomainService;
    private final OperatorDomainService operatorDomainService;

    /**
     * Registra el ingreso. Las filas de vehiculo y celda se leen con SELECT ... FOR UPDATE, de modo
     * que dos ingresos simultaneos del mismo vehiculo o sobre la misma celda se serializan y el
     * segundo es rechazado. Los indices unicos de la BD (AC1.5) son un respaldo adicional.
     */
    @Override
    public ParkingSession registerEntry(ParkingSession session) {

        Vehicle vehicle = lockAndValidateVehicle(session);
        ParkingSpot spot = lockAndValidateSpot(session, vehicle);
        Operator entryOperator = operatorDomainService.findById(session.getEntryOperatorId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el operador ingresado."));

        ParkingSession sessionToSave = session.registerEntry().toBuilder()
                .vehicle(vehicle)
                .spot(spot)
                .entryOperator(entryOperator)
                .build();
        ParkingSession savedSession = parkingSessionDomainService.save(sessionToSave);
        parkingSpotDomainService.switchToUnavailable(spot);

        return parkingSessionDomainService.findById(savedSession.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error al consultar la sesion guardada."));
    }

    private Vehicle lockAndValidateVehicle(ParkingSession session) {

        Long vehicleId = session.getVehicleId();
        Vehicle vehicle = vehicleDomainService.findByIdForUpdate(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el vehiculo ingresado."));

        if (parkingSessionDomainService.existsByVehicleIdAndNullExitTime(vehicleId)) {
            throw new BusinessRuleException("El vehículo ya se encuentra dentro del parqueadero (estancia activa).");
        }
        return vehicle;
    }

    private ParkingSpot lockAndValidateSpot(ParkingSession session, Vehicle vehicle) {

        ParkingSpot spot = parkingSpotDomainService.findByIdForUpdate(session.getSpotId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe la celda ingresada."));

        if (!spot.isAvailable()) {
            throw new BusinessRuleException("La celda " + spot.getSpotNumber() + " ya se encuentra ocupada.");
        }
        if (vehicle.getVehicleType() != null && spot.getVehicleType() != null
                && !Objects.equals(vehicle.getVehicleType().getId(), spot.getVehicleType().getId())) {
            throw new BusinessRuleException("La celda " + spot.getSpotNumber() + " no es apta para este tipo de vehículo.");
        }
        return spot;
    }
}
