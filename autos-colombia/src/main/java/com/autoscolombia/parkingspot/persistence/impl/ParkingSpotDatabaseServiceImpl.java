package com.autoscolombia.parkingspot.persistence.impl;

import com.autoscolombia.parkingspot.persistence.ParkingSpotDatabaseService;
import com.autoscolombia.parkingspot.persistence.entity.ParkingSpotEntity;
import com.autoscolombia.parkingspot.persistence.mapper.ParkingSpotMapper;
import com.autoscolombia.parkingspot.persistence.mapper.ParkingSpotSpecificationBuilderMapper;
import com.autoscolombia.parkingspot.persistence.repository.ParkingSpotRepository;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.parkingspot.service.domain.ParkingSpotQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingSpotDatabaseServiceImpl implements ParkingSpotDatabaseService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingSpotMapper mapper;
    private final ParkingSpotSpecificationBuilderMapper specificationMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ParkingSpot> findAll(Pageable pageable, ParkingSpotQuery query) {

        var spec = specificationMapper.mapToSpecificationBuilder(query)
                .buildSpecification();
        return parkingSpotRepository.findAll(spec, pageable)
                .map(mapper::mapToDomain);
    }

    @Override
    public ParkingSpot save(ParkingSpot parkingSpot) {

        ParkingSpotEntity spotToSave = computeDomainToSave(parkingSpot);
        ParkingSpotEntity savedSpot = parkingSpotRepository.save(spotToSave);
        return mapper.mapToDomain(savedSpot);
    }

    @Override
    public Optional<ParkingSpot> findById(Long id) {
        return parkingSpotRepository.findById(id)
                .map(mapper::mapToDomain);
    }

    private ParkingSpotEntity computeDomainToSave(ParkingSpot spot) {

        if (spot.getId() == null) {
            return mapper.mapToEntity(spot);
        }

        ParkingSpotEntity existingSpot = parkingSpotRepository.findById(spot.getId())
                .orElseThrow(() -> new RuntimeException("La celda a actualizar no existe en la base de datos."));

        return mapper.mapToExistingEntity(spot, existingSpot);
    }
}
