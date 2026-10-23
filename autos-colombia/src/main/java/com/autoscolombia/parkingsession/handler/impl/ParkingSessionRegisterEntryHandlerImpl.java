package com.autoscolombia.parkingsession.handler.impl;

import com.autoscolombia.parkingsession.handler.ParkingSessionRegisterEntryHandler;
import com.autoscolombia.parkingsession.service.ParkingSessionDomainService;
import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import com.autoscolombia.parkingspot.service.ParkingSpotDomainService;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.vehicle.service.VehicleDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ParkingSessionRegisterEntryHandlerImpl implements ParkingSessionRegisterEntryHandler {

    private final ParkingSessionDomainService parkingSessionDomainService;
    private final VehicleDomainService vehicleDomainService;
    private final ParkingSpotDomainService parkingSpotDomainService;

    @Override
    public ParkingSession registerEntry(ParkingSession session) {

        ParkingSpot loadedSpot = validateSpot(session);
        validateVehicle(session);
        session.registerEntry();
        ParkingSession savedSession = parkingSessionDomainService.save(session);
        parkingSpotDomainService.switchToUnavailable(loadedSpot);

        return parkingSessionDomainService.findById(savedSession.getId())
                .orElseThrow(() -> new RuntimeException("Error al consultar la sesion guardada."));
    }

    private ParkingSpot validateSpot(ParkingSession session) {

        ParkingSpot spot = parkingSpotDomainService.findById(session.getSpotId())
                .orElseThrow(() -> new RuntimeException("No existe la celda ingresada."));

        if (!spot.isAvailable()) {
            throw new RuntimeException("La celda seleccionada ya se encuentra ocupada.");
        }

        return spot;
    }

    private void validateVehicle(ParkingSession session) {

        Long vehicleId = session.getVehicleId();
        vehicleDomainService.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("No existe el vehiculo ingresado."));
        boolean isVehicleInActiveSession = parkingSessionDomainService.existsByVehicleIdAndNullExitTime(vehicleId);

        if (isVehicleInActiveSession) {
            throw new RuntimeException("El vehiculo se encuentra en otra sesion activa.");
        }
    }
}
