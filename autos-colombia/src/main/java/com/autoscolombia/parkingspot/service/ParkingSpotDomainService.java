package com.autoscolombia.parkingspot.service;

import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.parkingspot.service.domain.ParkingSpotQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ParkingSpotDomainService {

    Page<ParkingSpot> findAll(Pageable pageable, ParkingSpotQuery query);

    Optional<ParkingSpot> findById(Long id);

    /** Carga la celda bloqueándola hasta el fin de la transacción. */
    Optional<ParkingSpot> findByIdForUpdate(Long id);

    ParkingSpot createOrUpdate(ParkingSpot parkingSpot);

    /** Crea una celda disponible; 409 si el numero ya existe, 404 si el tipo no existe. */
    ParkingSpot create(ParkingSpot parkingSpot);

    /** Edita numero y tipo; 404 inexistente, 409 duplicada u ocupada. */
    ParkingSpot update(Long id, ParkingSpot changes);

    /** Elimina la celda; 404 inexistente, 409 si esta ocupada o tiene historial. */
    void delete(Long id);

    ParkingSpot switchToAvailable(ParkingSpot parkingSpot);

    ParkingSpot switchToUnavailable(ParkingSpot parkingSpot);
}
