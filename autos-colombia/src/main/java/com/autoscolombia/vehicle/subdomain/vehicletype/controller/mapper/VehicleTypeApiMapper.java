package com.autoscolombia.vehicle.subdomain.vehicletype.controller.mapper;

import com.autoscolombia.vehicle.subdomain.vehicletype.controller.dto.VehicleTypeViewApi;
import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleType;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface VehicleTypeApiMapper {

    VehicleTypeViewApi mapToViewApi(VehicleType domain);
}
