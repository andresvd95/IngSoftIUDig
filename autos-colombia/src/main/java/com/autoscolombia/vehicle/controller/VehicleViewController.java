package com.autoscolombia.vehicle.controller;

import com.autoscolombia.vehicle.controller.dto.VehicleCreationApi;
import com.autoscolombia.vehicle.controller.dto.VehicleViewApi;
import com.autoscolombia.vehicle.controller.mapper.VehicleApiMapper;
import com.autoscolombia.vehicle.service.VehicleDomainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("vehicle")
@RequiredArgsConstructor
public class VehicleViewController {

    private final VehicleDomainService vehicleDomainService;
    private final VehicleApiMapper apiMapper;

    /** GET /vehicle?licensePlate=ABC-123 (404 si el vehiculo no esta registrado). */
    @GetMapping(params = "licensePlate")
    public VehicleViewApi getByLicensePlate(@RequestParam String licensePlate) {
        return apiMapper.mapToViewApi(vehicleDomainService.getByLicensePlate(licensePlate));
    }

    /** GET /vehicle?customerId=1 : vehiculos de un cliente. */
    @GetMapping(params = "customerId")
    public List<VehicleViewApi> getByCustomer(@RequestParam Long customerId) {
        return vehicleDomainService.findByCustomerId(customerId).stream()
                .map(apiMapper::mapToViewApi)
                .toList();
    }

    /** POST /vehicle : 409 placa duplicada, 404 cliente o tipo inexistente. */
    @PostMapping
    public VehicleViewApi create(@Valid @RequestBody VehicleCreationApi creationApi) {
        return apiMapper.mapToViewApi(vehicleDomainService.create(apiMapper.mapToDomain(creationApi)));
    }
}
