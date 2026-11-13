package com.autoscolombia.vehicle.persistence.impl;

import com.autoscolombia.customer.persistence.repository.CustomerRepository;
import com.autoscolombia.vehicle.persistence.VehicleDatabaseService;
import com.autoscolombia.vehicle.persistence.entity.VehicleEntity;
import com.autoscolombia.vehicle.subdomain.vehicletype.persistence.repository.VehicleTypeRepository;
import com.autoscolombia.vehicle.persistence.mapper.VehicleMapper;
import com.autoscolombia.vehicle.persistence.repository.VehicleRepository;
import com.autoscolombia.vehicle.service.domain.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleDatabaseServiceImpl implements VehicleDatabaseService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper mapper;
    private final CustomerRepository customerRepository;
    private final VehicleTypeRepository vehicleTypeRepository;

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
    public Vehicle save(Vehicle vehicle) {
        VehicleEntity entity = VehicleEntity.builder()
                .licensePlate(vehicle.getLicensePlate())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .color(vehicle.getColor())
                .customer(customerRepository.getReferenceById(vehicle.getCustomer().getId()))
                .type(vehicleTypeRepository.getReferenceById(vehicle.getVehicleType().getId()))
                .build();
        return mapper.mapToDomain(vehicleRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findByCustomerId(Long customerId) {
        return vehicleRepository.findByCustomerIdOrderByLicensePlateAsc(customerId).stream()
                .map(mapper::mapToDomain)
                .toList();
    }

    @Override
    public Optional<Vehicle> findByIdForUpdate(Long id) {
        return vehicleRepository.findByIdForUpdate(id)
                .map(mapper::mapToDomain);
    }
}
