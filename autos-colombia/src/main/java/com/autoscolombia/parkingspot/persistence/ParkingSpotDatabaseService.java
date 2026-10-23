package com.autoscolombia.parkingspot.persistence;

import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.parkingspot.service.domain.ParkingSpotQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ParkingSpotDatabaseService {

    Page<ParkingSpot> findAll(Pageable pageable, ParkingSpotQuery query);

    ParkingSpot save(ParkingSpot parkingSpot);

    Optional<ParkingSpot> findById(Long id);
}
