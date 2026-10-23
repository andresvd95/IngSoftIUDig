package com.autoscolombia.vehicle.subdomain.vehicletype.persistence.mapper;

import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleType;
import com.autoscolombia.vehicle.subdomain.vehicletype.persistence.entity.VehicleTypeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleTypeMapper {

    VehicleType mapToDomain(VehicleTypeEntity entity);
}
