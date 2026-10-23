package com.autoscolombia.parkingsession.persistence;

import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ParkingSessionDatabaseService {

    ParkingSession save(ParkingSession session);

    boolean existsByVehicleIdAndNullExitTime(Long vehicleId);

    Optional<ParkingSession> findById(Long id);

    Page<ParkingSession> findAll(Pageable pageable);
}
