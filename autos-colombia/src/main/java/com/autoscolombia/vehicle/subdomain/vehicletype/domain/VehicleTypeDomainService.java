package com.autoscolombia.vehicle.subdomain.vehicletype.domain;

import java.util.List;

public interface VehicleTypeDomainService {

    List<VehicleType> fetchAll();

    /** Devuelve el tipo de vehiculo o lanza ResourceNotFoundException. */
    VehicleType getById(Long id);
}
