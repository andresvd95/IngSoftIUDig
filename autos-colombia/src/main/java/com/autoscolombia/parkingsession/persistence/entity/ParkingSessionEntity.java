package com.autoscolombia.parkingsession.persistence.entity;

import com.autoscolombia.common.persistence.entity.AuditEntity;
import com.autoscolombia.operator.persistence.entity.OperatorEntity;
import com.autoscolombia.parkingspot.persistence.entity.ParkingSpotEntity;
import com.autoscolombia.vehicle.persistence.entity.VehicleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_session")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSessionEntity extends AuditEntity {

    @Serial
    private static final long serialVersionUID = -8310444005949423906L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private VehicleEntity vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ParkingSpotEntity spot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_operator_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OperatorEntity entryOperator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exit_operator_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OperatorEntity exitOperator;
}
