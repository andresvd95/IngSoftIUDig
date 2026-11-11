package com.autoscolombia.vehicle.controller;

import com.autoscolombia.vehicle.controller.dto.VehicleViewApi;
import com.autoscolombia.vehicle.controller.mapper.VehicleApiMapper;
import com.autoscolombia.vehicle.service.VehicleDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vehicle")
@RequiredArgsConstructor
public class VehicleViewController {

    private final VehicleDomainService vehicleDomainService;
    private final VehicleApiMapper apiMapper;

    /** GET /vehicle?licensePlate=ABC-123 (404 si el vehiculo no esta registrado). */
    @GetMapping
    public VehicleViewApi getByLicensePlate(@RequestParam String licensePlate) {
        return apiMapper.mapToViewApi(vehicleDomainService.getByLicensePlate(licensePlate));
    }
}
