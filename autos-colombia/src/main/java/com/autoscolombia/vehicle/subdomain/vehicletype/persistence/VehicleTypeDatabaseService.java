package com.autoscolombia.vehicle.subdomain.vehicletype.persistence;

import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleType;

import java.util.List;

public interface VehicleTypeDatabaseService {

    List<VehicleType> findAll();
}
