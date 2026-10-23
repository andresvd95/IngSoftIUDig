package com.autoscolombia.parkingspot.service.impl;

import com.autoscolombia.parkingspot.persistence.ParkingSpotDatabaseService;
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

    @Override
    public Page<ParkingSpot> findAll(Pageable pageable, ParkingSpotQuery query) {
        return parkingSpotDatabaseService.findAll(pageable, query);
    }

    @Override
    public Optional<ParkingSpot> findById(Long id) {
        return parkingSpotDatabaseService.findById(id);
    }

    @Override
    public ParkingSpot createOrUpdate(ParkingSpot parkingSpot) {
        return parkingSpotDatabaseService.save(parkingSpot);
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
