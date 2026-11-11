package com.autoscolombia.parkingsession.handler.impl;

import com.autoscolombia.common.exception.BusinessRuleException;
import com.autoscolombia.common.exception.ResourceNotFoundException;
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

    /**
     * Cierra una estancia. La estancia se carga con SELECT ... FOR UPDATE, se valida que siga abierta
     * (sin salida registrada) y se libera la celda REAL de esa estancia, no la que envie el cliente.
     */
    @Override
    public ParkingSession registerExit(ParkingSession request) {

        ParkingSession storedSession = parkingSessionDomainService.findByIdForUpdate(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una estancia con el id " + request.getId() + ": no hay ingreso que cerrar."));

        if (storedSession.getExitTime() != null) {
            throw new BusinessRuleException("La estancia ya fue cerrada: el vehículo ya registró su salida.");
        }

        Operator exitOperator = operatorDomainService.findById(request.getExitOperatorId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el operador ingresado."));
        ParkingSpot spotToRelease = parkingSpotDomainService.findByIdForUpdate(storedSession.getSpotId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe la celda de la estancia."));

        ParkingSession sessionToSave = storedSession.registerExit().toBuilder()
                .exitOperator(exitOperator)
                .build();

        ParkingSession savedSession = parkingSessionDomainService.save(sessionToSave);
        parkingSpotDomainService.switchToAvailable(spotToRelease);

        return parkingSessionDomainService.findById(savedSession.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error al consultar la sesion guardada."));
    }
}
