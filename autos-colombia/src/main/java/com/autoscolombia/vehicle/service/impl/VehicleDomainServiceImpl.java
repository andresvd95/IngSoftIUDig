package com.autoscolombia.vehicle.service.impl;

import com.autoscolombia.common.exception.ResourceNotFoundException;
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

    @Override
    public Vehicle getByLicensePlate(String licensePlate) {
        return vehicleDatabaseService.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un vehículo registrado con la placa " + licensePlate + "."));
    }

    @Override
    public Optional<Vehicle> findByIdForUpdate(Long id) {
        return vehicleDatabaseService.findByIdForUpdate(id);
    }
}
