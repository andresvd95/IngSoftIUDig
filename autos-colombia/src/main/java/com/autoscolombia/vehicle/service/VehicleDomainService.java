package com.autoscolombia.vehicle.service;

import com.autoscolombia.vehicle.service.domain.Vehicle;

import java.util.Optional;

public interface VehicleDomainService {

    Optional<Vehicle> findById(Long id);
}
