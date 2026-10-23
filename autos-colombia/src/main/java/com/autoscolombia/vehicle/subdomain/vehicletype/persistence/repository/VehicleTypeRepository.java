package com.autoscolombia.vehicle.subdomain.vehicletype.persistence.repository;

import com.autoscolombia.vehicle.subdomain.vehicletype.persistence.entity.VehicleTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleTypeEntity, Long> {
}
