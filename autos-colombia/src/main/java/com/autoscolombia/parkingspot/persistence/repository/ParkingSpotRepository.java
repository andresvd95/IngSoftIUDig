package com.autoscolombia.parkingspot.persistence.repository;

import com.autoscolombia.parkingspot.persistence.entity.ParkingSpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotEntity, Long>,
        JpaSpecificationExecutor<ParkingSpotEntity> {
}
