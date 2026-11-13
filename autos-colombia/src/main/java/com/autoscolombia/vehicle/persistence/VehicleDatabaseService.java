package com.autoscolombia.vehicle.persistence;

import com.autoscolombia.vehicle.service.domain.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleDatabaseService {

    Optional<Vehicle> findById(Long id);

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    Optional<Vehicle> findByIdForUpdate(Long id);

    /** Guarda un vehiculo nuevo referenciando cliente y tipo por id. */
    Vehicle save(Vehicle vehicle);

    List<Vehicle> findByCustomerId(Long customerId);
}
