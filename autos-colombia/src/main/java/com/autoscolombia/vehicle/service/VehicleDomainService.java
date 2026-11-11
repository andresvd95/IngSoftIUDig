package com.autoscolombia.vehicle.service;

import com.autoscolombia.vehicle.service.domain.Vehicle;

import java.util.Optional;

public interface VehicleDomainService {

    Optional<Vehicle> findById(Long id);

    /** Devuelve el vehiculo registrado con esa placa o lanza ResourceNotFoundException. */
    Vehicle getByLicensePlate(String licensePlate);

    Optional<Vehicle> findByIdForUpdate(Long id);
}
