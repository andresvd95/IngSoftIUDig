package com.autoscolombia.vehicle.persistence.mapper;

import com.autoscolombia.vehicle.persistence.entity.VehicleEntity;
import com.autoscolombia.vehicle.service.domain.Vehicle;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface VehicleMapper {

    Vehicle mapToDomain(VehicleEntity vehicleEntity);
}
