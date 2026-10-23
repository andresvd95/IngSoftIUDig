package com.autoscolombia.vehicle.service.domain;

import com.autoscolombia.common.service.domain.Audit;
import com.autoscolombia.customer.service.domain.Customer;
import com.autoscolombia.vehicle.subdomain.vehicletype.domain.VehicleType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Vehicle extends Audit {

    @Serial
    private static final long serialVersionUID = 2070627258865824820L;

    private Long id;

    @NotEmpty(message = "La placa es obligatoria.")
    @Size(max = 20, message = "La placa no debe exceder los {max} caracteres.")
    private String licensePlate;

    @Size(max = 50, message = "La marca no debe exceder los {max} caracteres.")
    private String brand;

    private Integer model;

    @Size(max = 50, message = "El color no debe exceder los {max} caracteres.")
    private String color;

    @NotNull(message = "El tipo de vehículo es obligatorio.")
    private VehicleType vehicleType;

    @NotNull(message = "El cliente asociado es obligatorio.")
    private Customer customer;
}