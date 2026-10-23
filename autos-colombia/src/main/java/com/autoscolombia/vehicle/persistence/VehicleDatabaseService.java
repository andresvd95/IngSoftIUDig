package com.autoscolombia.vehicle.persistence;

import com.autoscolombia.vehicle.service.domain.Vehicle;

import java.util.Optional;

public interface VehicleDatabaseService {

    Optional<Vehicle> findById(Long id);
}
