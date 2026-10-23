package com.autoscolombia.parkingsession.persistence.mapper;

import com.autoscolombia.parkingsession.persistence.entity.ParkingSessionEntity;
import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import org.mapstruct.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ParkingSessionMapper {

    ParkingSessionEntity mapToEntity(ParkingSession session);

    ParkingSession mapToDomain(ParkingSessionEntity sessionEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ParkingSessionEntity mapToExistingEntity(ParkingSession domain, @MappingTarget ParkingSessionEntity entity);

}
