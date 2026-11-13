package com.autoscolombia.vehicle.controller.mapper;

import com.autoscolombia.vehicle.controller.dto.VehicleViewApi;
import com.autoscolombia.vehicle.service.domain.Vehicle;
import com.autoscolombia.vehicle.subdomain.vehicletype.controller.mapper.VehicleTypeApiMapper;
import com.autoscolombia.vehicle.controller.dto.VehicleCreationApi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = VehicleTypeApiMapper.class)
public interface VehicleApiMapper {

    VehicleViewApi mapToViewApi(Vehicle domain);

    @Mapping(target = "customer.id", source = "customerId")
    @Mapping(target = "vehicleType.id", source = "vehicleTypeId")
    Vehicle mapToDomain(VehicleCreationApi creationApi);
}
