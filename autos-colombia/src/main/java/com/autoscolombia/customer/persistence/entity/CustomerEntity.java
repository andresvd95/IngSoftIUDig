package com.autoscolombia.customer.persistence.entity;

import com.autoscolombia.common.persistence.entity.AuditEntity;
import com.autoscolombia.vehicle.persistence.entity.VehicleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.Set;

@Entity
@Table(name = "customer")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity extends AuditEntity {

    @Serial
    private static final long serialVersionUID = -4235497510011977370L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "identification_type")
    private String identificationType;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<VehicleEntity> vehicles;
}
