package com.autoscolombia.parkingsession.persistence;

import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ParkingSessionDatabaseService {

    ParkingSession save(ParkingSession session);

    boolean existsByVehicleIdAndNullExitTime(Long vehicleId);

    boolean existsOpenBySpotId(Long spotId);

    boolean existsAnyBySpotId(Long spotId);

    boolean existsOpenByCustomerId(Long customerId);

    boolean existsAnyByCustomerId(Long customerId);

    Optional<ParkingSession> findById(Long id);

    Optional<ParkingSession> findByIdForUpdate(Long id);

    Page<ParkingSession> findAll(Pageable pageable);

    Page<ParkingSession> findHistory(Pageable pageable);

    List<ParkingSession> findActive();
}
