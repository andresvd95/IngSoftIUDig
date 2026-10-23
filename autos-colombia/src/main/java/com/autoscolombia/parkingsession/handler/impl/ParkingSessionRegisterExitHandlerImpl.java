package com.autoscolombia.parkingsession.handler.impl;

import com.autoscolombia.operator.service.OperatorDomainService;
import com.autoscolombia.operator.service.domain.Operator;
import com.autoscolombia.parkingsession.handler.ParkingSessionRegisterExitHandler;
import com.autoscolombia.parkingsession.service.ParkingSessionDomainService;
import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import com.autoscolombia.parkingspot.service.ParkingSpotDomainService;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ParkingSessionRegisterExitHandlerImpl implements ParkingSessionRegisterExitHandler {

    private final ParkingSessionDomainService parkingSessionDomainService;
    private final ParkingSpotDomainService parkingSpotDomainService;
    private final OperatorDomainService operatorDomainService;

    @Override
    public ParkingSession registerExit(ParkingSession session) {

        ParkingSpot spotToRelease = parkingSpotDomainService.findById(session.getSpotId())
                .orElseThrow(() -> new RuntimeException("No existe la celda ingresada."));
        Operator exitOperator = operatorDomainService.findById(session.getExitOperatorId())
                .orElseThrow(() -> new RuntimeException("No existe el operador ingresado."));

        ParkingSession sessionToSave = session.registerExit().toBuilder()
                .exitOperator(exitOperator)
                .build();

        ParkingSession savedSession = parkingSessionDomainService.save(sessionToSave);
        parkingSpotDomainService.switchToAvailable(spotToRelease);

        return parkingSessionDomainService.findById(savedSession.getId())
                .orElseThrow(() -> new RuntimeException("Error al consultar la sesion guardada."));
    }

}