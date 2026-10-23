package com.autoscolombia.operator.service.domain;

import com.autoscolombia.common.service.domain.Audit;
import jakarta.validation.constraints.NotEmpty;
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
public class Operator extends Audit {

    @Serial
    private static final long serialVersionUID = 2390698867503071948L;

    private Long id;

    @NotEmpty(message = "El nombre del operador es obligatorio.")
    @Size(max = 100, message = "El nombre no debe exceder los {max} caracteres.")
    private String name;

    @NotEmpty(message = "El número de identificación es obligatorio.")
    @Size(max = 20, message = "La identificación no debe exceder los {max} caracteres.")
    private String identificationNumber;

    @NotEmpty(message = "El tipo de identificación es obligatorio.")
    @Size(max = 10, message = "El tipo de identificación no debe exceder los {max} caracteres.")
    private String identificationType;

    @NotEmpty(message = "El nombre de usuario es obligatorio.")
    @Size(max = 50, message = "El nombre de usuario no debe exceder los {max} caracteres.")
    private String username;
}