package com.autoscolombia.parkingsession.persistence.repository;

import com.autoscolombia.parkingsession.persistence.entity.ParkingSessionEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSessionEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"vehicle", "spot", "entryOperator", "exitOperator"})
    Optional<ParkingSessionEntity> findById(@NonNull Long id);

    @Override
    @EntityGraph(attributePaths = {"vehicle", "spot", "entryOperator", "exitOperator"})
    Page<ParkingSessionEntity> findAll(@NonNull Pageable pageable);

    boolean existsByVehicleIdAndExitTimeIsNull(Long vehicleId);
}
