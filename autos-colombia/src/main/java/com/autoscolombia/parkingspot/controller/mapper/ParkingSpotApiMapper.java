package com.autoscolombia.parkingspot.controller.mapper;

import com.autoscolombia.parkingspot.controller.dto.ParkingSpotCreationApi;
import com.autoscolombia.parkingspot.controller.dto.ParkingSpotViewApi;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ParkingSpotApiMapper {

    ParkingSpotViewApi mapToViewApi(ParkingSpot parkingSpot);

    ParkingSpot mapToDomain(ParkingSpotCreationApi creationApi);
}
