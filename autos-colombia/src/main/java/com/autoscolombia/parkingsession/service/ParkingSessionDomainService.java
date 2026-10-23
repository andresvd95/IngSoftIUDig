package com.autoscolombia.parkingsession.service;

import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ParkingSessionDomainService {

    ParkingSession save(ParkingSession parkingSession);

    Optional<ParkingSession> findById(Long id);

    boolean existsByVehicleIdAndNullExitTime(Long vehicleId);

    Page<ParkingSession> findAll(Pageable pageable);
}
