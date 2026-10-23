package com.autoscolombia.operator.persistence.entity;

import com.autoscolombia.common.persistence.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@Entity
@Table(name = "operator")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OperatorEntity extends AuditEntity {
    @Serial
    private static final long serialVersionUID = 421480017665599961L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "identification_type")
    private String identificationType;

    @Column(name = "username")
    private String username;
}
