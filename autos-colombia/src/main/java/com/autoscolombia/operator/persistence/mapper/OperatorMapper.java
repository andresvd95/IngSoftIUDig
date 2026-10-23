package com.autoscolombia.operator.persistence.mapper;

import com.autoscolombia.operator.persistence.entity.OperatorEntity;
import com.autoscolombia.operator.service.domain.Operator;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OperatorMapper {

    Operator mapToDomain(OperatorEntity entity);
}
