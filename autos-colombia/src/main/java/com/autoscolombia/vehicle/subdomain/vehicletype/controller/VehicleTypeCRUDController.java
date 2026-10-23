package com.autoscolombia.vehicle.subdomain.vehicletype.controller;

import com.autoscolombia.vehicle.subdomain.vehicletype.controller.dto.VehicleTypeViewApi;
import com.autoscolombia.vehicle.subdomain.vehicletype.controller.mapper.VehicleTypeApiMapper;
import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleTypeDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("vehicle-type")
@RequiredArgsConstructor
public class VehicleTypeCRUDController {

    private final VehicleTypeDomainService vehicleTypeDomainService;
    private final VehicleTypeApiMapper apiMapper;

    @GetMapping
    public List<VehicleTypeViewApi> getVehicleTypes() {

        return vehicleTypeDomainService.fetchAll()
                .stream()
                .map(apiMapper::mapToViewApi)
                .toList();
    }
}
