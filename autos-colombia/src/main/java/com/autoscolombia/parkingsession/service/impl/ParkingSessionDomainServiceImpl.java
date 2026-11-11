package com.autoscolombia.parkingsession.service.impl;

import com.autoscolombia.parkingsession.persistence.ParkingSessionDatabaseService;
import com.autoscolombia.parkingsession.service.ParkingSessionDomainService;
import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingSessionDomainServiceImpl implements ParkingSessionDomainService {

    private final ParkingSessionDatabaseService parkingSessionDatabaseService;

    @Override
    public ParkingSession save(ParkingSession parkingSession) {
        return parkingSessionDatabaseService.save(parkingSession);
    }

    @Override
    public Optional<ParkingSession> findById(Long id) {
        return parkingSessionDatabaseService.findById(id);
    }

    @Override
    public Optional<ParkingSession> findByIdForUpdate(Long id) {
        return parkingSessionDatabaseService.findByIdForUpdate(id);
    }

    @Override
    public boolean existsByVehicleIdAndNullExitTime(Long vehicleId) {
        return parkingSessionDatabaseService.existsByVehicleIdAndNullExitTime(vehicleId);
    }

    @Override
    public Page<ParkingSession> findAll(Pageable pageable) {
        return parkingSessionDatabaseService.findAll(pageable);
    }

    @Override
    public Page<ParkingSession> findHistory(Pageable pageable) {
        return parkingSessionDatabaseService.findHistory(pageable);
    }

    @Override
    public List<ParkingSession> findActive() {
        return parkingSessionDatabaseService.findActive();
    }
}
