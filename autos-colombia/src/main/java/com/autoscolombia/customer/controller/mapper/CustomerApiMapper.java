package com.autoscolombia.customer.controller.mapper;

import com.autoscolombia.customer.controller.dto.CustomerCreationApi;
import com.autoscolombia.customer.controller.dto.CustomerViewApi;
import com.autoscolombia.customer.service.domain.Customer;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CustomerApiMapper {

    CustomerViewApi mapToViewApi(Customer domain);

    Customer mapToDomain(CustomerCreationApi creationApi);
}
