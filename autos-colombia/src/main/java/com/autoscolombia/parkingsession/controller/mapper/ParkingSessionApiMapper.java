package com.autoscolombia.parkingsession.controller.mapper;

import com.autoscolombia.parkingsession.controller.dto.ParkingSessionExitCreationApi;
import com.autoscolombia.parkingsession.controller.dto.ParkingSessionIngressCreationApi;
import com.autoscolombia.parkingsession.controller.dto.ParkingSessionViewApi;
import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ParkingSessionApiMapper {

    @Mapping(source = "vehicleId", target = "vehicle.id")
    @Mapping(source = "spotId", target = "spot.id")
    @Mapping(source = "entryOperatorId", target = "entryOperator.id")
    @Mapping(target = "entryTime", ignore = true)
    @Mapping(target = "exitTime", ignore = true)
    @Mapping(target = "exitOperator", ignore = true)
    ParkingSession mapToDomain(ParkingSessionIngressCreationApi creationApi);


    @Mapping(source = "spotId", target = "spot.id")
    @Mapping(source = "exitOperatorId", target = "exitOperator.id")
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "entryTime", ignore = true)
    @Mapping(target = "exitTime", ignore = true)
    @Mapping(target = "exitOperator", ignore = true)
    ParkingSession mapToDomain(ParkingSessionExitCreationApi creationApi);

    ParkingSessionViewApi mapToViewApi(ParkingSession domain);
}
