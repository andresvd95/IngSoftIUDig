package com.autoscolombia.vehicle.subdomain.vehicletype.domain.impl;

import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleType;
import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleTypeDomainService;
import com.autoscolombia.vehicle.subdomain.vehicletype.persistence.VehicleTypeDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleTypeDomainServiceImpl implements VehicleTypeDomainService {

    private final VehicleTypeDatabaseService vehicleTypeDatabaseService;

    @Override
    public List<VehicleType> fetchAll() {
        return vehicleTypeDatabaseService.findAll();
    }
}
