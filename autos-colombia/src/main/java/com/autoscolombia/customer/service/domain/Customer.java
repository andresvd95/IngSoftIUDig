package com.autoscolombia.customer.service.domain;

import com.autoscolombia.common.service.domain.Audit;
import jakarta.validation.constraints.Email;
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
public class Customer extends Audit {

    @Serial
    private static final long serialVersionUID = 2086106835971205550L;

    private Long id;

    @NotEmpty(message = "El número de identificación es obligatorio.")
    @Size(max = 20, message = "La identificación no debe exceder los {max} caracteres.")
    private String identificationNumber;

    @NotEmpty(message = "El tipo de identificación es obligatorio.")
    @Size(max = 10, message = "El tipo de identificación no debe exceder los {max} caracteres.")
    private String identificationType;

    @NotEmpty(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no debe exceder los {max} caracteres.")
    private String name;

    @Email(message = "Debe ser un correo electrónico válido.")
    @Size(max = 100, message = "El correo no debe exceder los {max} caracteres.")
    private String email;

    @Size(max = 20, message = "El teléfono no debe exceder los {max} caracteres.")
    private String phone;
}