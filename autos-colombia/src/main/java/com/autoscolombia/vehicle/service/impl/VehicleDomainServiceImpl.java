package com.autoscolombia.vehicle.service.impl;

import com.autoscolombia.vehicle.persistence.VehicleDatabaseService;
import com.autoscolombia.vehicle.service.VehicleDomainService;
import com.autoscolombia.vehicle.service.domain.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleDomainServiceImpl implements VehicleDomainService {

    private final VehicleDatabaseService vehicleDatabaseService;

    @Override
    public Optional<Vehicle> findById(Long id) {
        return vehicleDatabaseService.findById(id);
    }
}
