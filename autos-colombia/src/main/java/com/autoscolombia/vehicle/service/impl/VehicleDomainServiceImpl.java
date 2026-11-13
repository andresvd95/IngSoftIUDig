package com.autoscolombia.vehicle.service.impl;

import com.autoscolombia.common.exception.ResourceNotFoundException;
import com.autoscolombia.common.exception.BusinessRuleException;
import com.autoscolombia.customer.service.CustomerDomainService;
import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleTypeDomainService;
import com.autoscolombia.vehicle.persistence.VehicleDatabaseService;
import com.autoscolombia.vehicle.service.VehicleDomainService;
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
public class VehicleDomainServiceImpl implements VehicleDomainService {

    private final VehicleDatabaseService vehicleDatabaseService;
    private final CustomerDomainService customerDomainService;
    private final VehicleTypeDomainService vehicleTypeDomainService;

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

    @Override
    public Vehicle create(Vehicle vehicle) {
        String plate = vehicle.getLicensePlate().trim().toUpperCase(Locale.ROOT);
        if (vehicleDatabaseService.findByLicensePlate(plate).isPresent()) {
            throw new BusinessRuleException("Ya existe un vehículo registrado con la placa " + plate + ".");
        }
        customerDomainService.getById(vehicle.getCustomer().getId());
        vehicleTypeDomainService.getById(vehicle.getVehicleType().getId());
        return vehicleDatabaseService.save(vehicle.toBuilder().id(null).licensePlate(plate).build());
    }

    @Override
    public List<Vehicle> findByCustomerId(Long customerId) {
        customerDomainService.getById(customerId);
        return vehicleDatabaseService.findByCustomerId(customerId);
    }
}
