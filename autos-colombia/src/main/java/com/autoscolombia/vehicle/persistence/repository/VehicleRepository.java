package com.autoscolombia.vehicle.persistence.repository;

import com.autoscolombia.vehicle.persistence.entity.VehicleEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"customer", "type"})
    Optional<VehicleEntity> findById(Long id);

    /** Busca por placa ignorando mayusculas, espacios y guiones (ABC 123 = abc-123). */
    @EntityGraph(attributePaths = {"customer", "type"})
    @Query("select v from VehicleEntity v where upper(replace(replace(v.licensePlate, '-', ''), ' ', '')) = :plate")
    Optional<VehicleEntity> findByNormalizedPlate(@Param("plate") String plate);

    @EntityGraph(attributePaths = {"customer", "type"})
    List<VehicleEntity> findByCustomerIdOrderByLicensePlateAsc(Long customerId);

    /** SELECT ... FOR UPDATE: serializa los ingresos simultaneos del mismo vehiculo. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from VehicleEntity v where v.id = :id")
    Optional<VehicleEntity> findByIdForUpdate(@Param("id") Long id);
}
