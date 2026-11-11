package com.autoscolombia.vehicle.persistence.impl;

import com.autoscolombia.vehicle.persistence.VehicleDatabaseService;
import com.autoscolombia.vehicle.persistence.mapper.VehicleMapper;
import com.autoscolombia.vehicle.persistence.repository.VehicleRepository;
import com.autoscolombia.vehicle.service.domain.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleDatabaseServiceImpl implements VehicleDatabaseService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper mapper;

    @Override
    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id)
                .map(mapper::mapToDomain);
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        String normalized = licensePlate == null ? "" : licensePlate.replaceAll("[\s-]", "").toUpperCase(Locale.ROOT);
        return vehicleRepository.findByNormalizedPlate(normalized)
                .map(mapper::mapToDomain);
    }

    @Override
    public Optional<Vehicle> findByIdForUpdate(Long id) {
        return vehicleRepository.findByIdForUpdate(id)
                .map(mapper::mapToDomain);
    }
}
