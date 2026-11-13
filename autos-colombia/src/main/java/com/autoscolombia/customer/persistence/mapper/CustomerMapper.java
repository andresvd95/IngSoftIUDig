package com.autoscolombia.customer.persistence.mapper;

import com.autoscolombia.customer.persistence.entity.CustomerEntity;
import com.autoscolombia.customer.service.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CustomerMapper {

    Customer mapToDomain(CustomerEntity entity);

    @Mapping(target = "vehicles", ignore = true)
    CustomerEntity mapToEntity(Customer domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    CustomerEntity mapToExistingEntity(Customer domain, @MappingTarget CustomerEntity entity);
}
