package com.autoscolombia.customer.service;

import com.autoscolombia.customer.service.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerDomainService {

    /** Crea un cliente; BusinessRuleException si ya existe el mismo tipo + número (o correo). */
    Customer create(Customer customer);

    /** Actualiza un cliente; 404 si no existe, BusinessRuleException si choca con otro. */
    Customer update(Long id, Customer customer);

    Page<Customer> search(String search, Pageable pageable);

    /** Devuelve el cliente o lanza ResourceNotFoundException. */
    Customer getById(Long id);

    /** Elimina el cliente; BusinessRuleException si algún vehículo suyo está dentro del parqueadero. */
    void delete(Long id);
}
