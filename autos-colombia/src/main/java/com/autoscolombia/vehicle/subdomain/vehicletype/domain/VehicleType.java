package com.autoscolombia.vehicle.subdomain.vehicletype.domain;

import com.autoscolombia.common.service.domain.Audit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.math.BigDecimal;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VehicleType extends Audit {

    @Serial
    private static final long serialVersionUID = 1463868513871686353L;

    private Long id;

    @NotEmpty(message = "El numero de la celda no debe estar vacio.")
    @Size(max = 50, message = "El numero de la celda no debe ser mayor a {max} caracteres.")
    private String name;

    @Size(max = 255, message = "El numero de la celda no debe ser mayor a {max} caracteres.")
    private String description;

    @NotNull(message = "La tarifa mensual es obligatoria.")
    @DecimalMin(value = "0.0", message = "La tarifa debe ser mayor o igual a 0")
    private BigDecimal monthlyRate;
}
