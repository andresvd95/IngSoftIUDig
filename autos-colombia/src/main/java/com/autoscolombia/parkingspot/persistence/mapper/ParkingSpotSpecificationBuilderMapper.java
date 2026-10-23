package com.autoscolombia.parkingspot.persistence.mapper;


import com.autoscolombia.parkingspot.persistence.builder.ParkingSpotSpecificationBuilder;
import com.autoscolombia.parkingspot.service.domain.ParkingSpotQuery;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ParkingSpotSpecificationBuilderMapper {

    ParkingSpotSpecificationBuilder mapToSpecificationBuilder(ParkingSpotQuery query);
}
