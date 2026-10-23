package com.autoscolombia.parkingspot.persistence.mapper;

import com.autoscolombia.parkingspot.persistence.entity.ParkingSpotEntity;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ParkingSpotMapper {

    ParkingSpot mapToDomain(ParkingSpotEntity entity);

    ParkingSpotEntity mapToEntity(ParkingSpot domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ParkingSpotEntity mapToExistingEntity(ParkingSpot domain, @MappingTarget ParkingSpotEntity entity);
}
