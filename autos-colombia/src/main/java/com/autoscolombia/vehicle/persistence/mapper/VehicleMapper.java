package com.autoscolombia.vehicle.persistence.mapper;

import com.autoscolombia.vehicle.persistence.entity.VehicleEntity;
import com.autoscolombia.vehicle.service.domain.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface VehicleMapper {

    @Mapping(source = "type", target = "vehicleType")
    Vehicle mapToDomain(VehicleEntity vehicleEntity);
}
