package com.autoscolombia.parkingspot.persistence.entity;

import com.autoscolombia.common.persistence.entity.AuditEntity;
import com.autoscolombia.vehicle.subdomain.vehicletype.persistence.entity.VehicleTypeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@Entity
@Table(name = "parking_spot")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpotEntity extends AuditEntity {

    @Serial
    private static final long serialVersionUID = -6644865390318472227L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "spot_number")
    private String spotNumber;

    @Column(name = "is_available")
    private boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_type_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private VehicleTypeEntity vehicleType;
}
