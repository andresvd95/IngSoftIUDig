package com.autoscolombia.customer.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

public record CustomerCreationApi(
        @NotBlank(message = "El tipo de identificación es obligatorio.")
        @Size(max = 10, message = "El tipo de identificación no debe exceder los {max} caracteres.")
        String identificationType,

        @NotBlank(message = "El número de identificación es obligatorio.")
        @Size(max = 20, message = "La identificación no debe exceder los {max} caracteres.")
        String identificationNumber,

        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 100, message = "El nombre no debe exceder los {max} caracteres.")
        String name,

        @Email(message = "Debe ser un correo electrónico válido.")
        @Size(max = 100, message = "El correo no debe exceder los {max} caracteres.")
        String email,

        @Size(max = 20, message = "El teléfono no debe exceder los {max} caracteres.")
        String phone) implements Serializable {

    @Serial
    private static final long serialVersionUID = 3417752398113207742L;
}
