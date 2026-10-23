package com.autoscolombia.vehicle.subdomain.vehicletype.persistence.impl;

import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleType;
import com.autoscolombia.vehicle.subdomain.vehicletype.persistence.VehicleTypeDatabaseService;
import com.autoscolombia.vehicle.subdomain.vehicletype.persistence.mapper.VehicleTypeMapper;
import com.autoscolombia.vehicle.subdomain.vehicletype.persistence.repository.VehicleTypeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleTypeDatabaseServiceImpl implements VehicleTypeDatabaseService {

    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleTypeMapper mapper;

    @Override
    public List<VehicleType> findAll() {

        return CollectionUtils.emptyIfNull(vehicleTypeRepository.findAll())
                .stream()
                .map(mapper::mapToDomain)
                .toList();
    }
}
