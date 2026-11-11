package com.autoscolombia.parkingspot.persistence.repository;

import com.autoscolombia.parkingspot.persistence.entity.ParkingSpotEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotEntity, Long>,
        JpaSpecificationExecutor<ParkingSpotEntity> {

    /** SELECT ... FOR UPDATE: serializa la asignación simultánea de una misma celda. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ParkingSpotEntity s where s.id = :id")
    Optional<ParkingSpotEntity> findByIdForUpdate(@Param("id") Long id);
}
