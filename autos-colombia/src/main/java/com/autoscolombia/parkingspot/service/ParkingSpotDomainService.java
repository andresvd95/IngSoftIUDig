package com.autoscolombia.parkingspot.service;

import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.parkingspot.service.domain.ParkingSpotQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ParkingSpotDomainService {

    Page<ParkingSpot> findAll(Pageable pageable, ParkingSpotQuery query);

    Optional<ParkingSpot> findById(Long id);

    ParkingSpot createOrUpdate(ParkingSpot parkingSpot);

    ParkingSpot switchToAvailable(ParkingSpot parkingSpot);

    ParkingSpot switchToUnavailable(ParkingSpot parkingSpot);
}
