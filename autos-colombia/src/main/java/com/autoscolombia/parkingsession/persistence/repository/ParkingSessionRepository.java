package com.autoscolombia.parkingsession.persistence.repository;

import com.autoscolombia.parkingsession.persistence.entity.ParkingSessionEntity;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSessionEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"vehicle", "vehicle.customer", "vehicle.type", "spot", "entryOperator", "exitOperator"})
    Optional<ParkingSessionEntity> findById(@NonNull Long id);

    @Override
    @EntityGraph(attributePaths = {"vehicle", "vehicle.customer", "vehicle.type", "spot", "entryOperator", "exitOperator"})
    Page<ParkingSessionEntity> findAll(@NonNull Pageable pageable);

    /** Historial: estancias ya cerradas. */
    @EntityGraph(attributePaths = {"vehicle", "vehicle.customer", "vehicle.type", "spot", "entryOperator", "exitOperator"})
    Page<ParkingSessionEntity> findByExitTimeIsNotNull(Pageable pageable);

    /** Vehiculos actualmente dentro (estancias abiertas), las mas recientes primero. */
    @EntityGraph(attributePaths = {"vehicle", "vehicle.customer", "vehicle.type", "spot", "entryOperator", "exitOperator"})
    List<ParkingSessionEntity> findByExitTimeIsNullOrderByEntryTimeDesc();

    boolean existsByVehicleIdAndExitTimeIsNull(Long vehicleId);

    boolean existsBySpotIdAndExitTimeIsNull(Long spotId);

    boolean existsBySpotId(Long spotId);

    boolean existsByVehicleCustomerIdAndExitTimeIsNull(Long customerId);

    boolean existsByVehicleCustomerId(Long customerId);

    /** SELECT ... FOR UPDATE de la estancia: evita cerrarla dos veces en simultaneo. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ParkingSessionEntity s where s.id = :id")
    Optional<ParkingSessionEntity> findByIdForUpdate(@Param("id") Long id);
}
