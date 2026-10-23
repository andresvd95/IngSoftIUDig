package com.autoscolombia.vehicle.subdomain.vehicletype.persistence.entity;

import com.autoscolombia.common.persistence.entity.AuditEntity;
import com.autoscolombia.vehicle.persistence.entity.VehicleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "vehicle_type")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class VehicleTypeEntity extends AuditEntity {

    @Serial
    private static final long serialVersionUID = -2922539303397487316L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "monthly_rate")
    private BigDecimal monthlyRate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<VehicleEntity> vehicles;
}