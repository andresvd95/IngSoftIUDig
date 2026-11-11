package com.autoscolombia.parkingsession.service;

import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ParkingSessionDomainService {

    ParkingSession save(ParkingSession parkingSession);

    Optional<ParkingSession> findById(Long id);

    Optional<ParkingSession> findByIdForUpdate(Long id);

    boolean existsByVehicleIdAndNullExitTime(Long vehicleId);

    Page<ParkingSession> findAll(Pageable pageable);

    /** Estancias cerradas (historial). */
    Page<ParkingSession> findHistory(Pageable pageable);

    /** Vehiculos que estan dentro del parqueadero en este momento. */
    List<ParkingSession> findActive();
}
