package com.autoscolombia.parkingspot.service.impl;

import com.autoscolombia.common.exception.BusinessRuleException;
import com.autoscolombia.common.exception.ResourceNotFoundException;
import com.autoscolombia.parkingsession.persistence.ParkingSessionDatabaseService;
import com.autoscolombia.parkingspot.persistence.ParkingSpotDatabaseService;
import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleTypeDomainService;
import com.autoscolombia.parkingspot.service.ParkingSpotDomainService;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.parkingspot.service.domain.ParkingSpotQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingSpotDomainServiceImpl implements ParkingSpotDomainService {

    private final ParkingSpotDatabaseService parkingSpotDatabaseService;
    private final ParkingSessionDatabaseService parkingSessionDatabaseService;
    private final VehicleTypeDomainService vehicleTypeDomainService;

    @Override
    public Page<ParkingSpot> findAll(Pageable pageable, ParkingSpotQuery query) {
        return parkingSpotDatabaseService.findAll(pageable, query);
    }

    @Override
    public Optional<ParkingSpot> findById(Long id) {
        return parkingSpotDatabaseService.findById(id);
    }

    @Override
    public Optional<ParkingSpot> findByIdForUpdate(Long id) {
        return parkingSpotDatabaseService.findByIdForUpdate(id);
    }

    @Override
    public ParkingSpot createOrUpdate(ParkingSpot parkingSpot) {
        return parkingSpotDatabaseService.save(parkingSpot);
    }

    @Override
    public ParkingSpot create(ParkingSpot parkingSpot) {
        String number = normalizeNumber(parkingSpot);
        validateNumberIsFree(number, null);
        var type = vehicleTypeDomainService.getById(requireTypeId(parkingSpot));
        return parkingSpotDatabaseService.save(ParkingSpot.builder()
                .spotNumber(number)
                .available(true)
                .vehicleType(type)
                .build());
    }

    @Override
    public ParkingSpot update(Long id, ParkingSpot changes) {
        ParkingSpot existing = parkingSpotDatabaseService.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la celda con id " + id + "."));
        if (parkingSessionDatabaseService.existsOpenBySpotId(id)) {
            throw new BusinessRuleException("No se puede editar la celda: está ocupada.");
        }
        String number = normalizeNumber(changes);
        validateNumberIsFree(number, id);
        var type = vehicleTypeDomainService.getById(requireTypeId(changes));
        return parkingSpotDatabaseService.save(existing.toBuilder()
                .spotNumber(number)
                .vehicleType(type)
                .build());
    }

    @Override
    public void delete(Long id) {
        parkingSpotDatabaseService.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la celda con id " + id + "."));
        if (parkingSessionDatabaseService.existsOpenBySpotId(id)) {
            throw new BusinessRuleException("No se puede eliminar la celda: está ocupada.");
        }
        if (parkingSessionDatabaseService.existsAnyBySpotId(id)) {
            throw new BusinessRuleException("No se puede eliminar la celda: tiene historial de estancias.");
        }
        parkingSpotDatabaseService.deleteById(id);
    }

    private String normalizeNumber(ParkingSpot spot) {
        if (spot.getSpotNumber() == null || spot.getSpotNumber().isBlank()) {
            throw new IllegalArgumentException("El número de la celda es obligatorio.");
        }
        return spot.getSpotNumber().trim().toUpperCase(java.util.Locale.ROOT);
    }

    private Long requireTypeId(ParkingSpot spot) {
        if (spot.getVehicleType() == null || spot.getVehicleType().getId() == null) {
            throw new IllegalArgumentException("El tipo de vehículo es obligatorio.");
        }
        return spot.getVehicleType().getId();
    }

    private void validateNumberIsFree(String number, Long excludeId) {
        if (parkingSpotDatabaseService.existsBySpotNumber(number, excludeId)) {
            throw new BusinessRuleException("Ya existe una celda con el número " + number + ".");
        }
    }

    @Override
    public ParkingSpot switchToAvailable(ParkingSpot parkingSpot) {

        return Optional.of(parkingSpot)
                .map(ParkingSpot::switchToAvailable)
                .map(this::createOrUpdate)
                .orElseThrow(() -> new RuntimeException("Error actualizando el estado de la celda."));
    }

    @Override
    public ParkingSpot switchToUnavailable(ParkingSpot parkingSpot) {

        return Optional.of(parkingSpot)
                .map(ParkingSpot::switchToUnavailable)
                .map(this::createOrUpdate)
                .orElseThrow(() -> new RuntimeException("Error actualizando el estado de la celda."));
    }
}
