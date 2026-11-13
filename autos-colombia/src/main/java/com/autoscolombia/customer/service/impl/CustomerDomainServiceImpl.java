package com.autoscolombia.customer.service.impl;

import com.autoscolombia.common.exception.BusinessRuleException;
import com.autoscolombia.common.exception.ResourceNotFoundException;
import com.autoscolombia.customer.persistence.CustomerDatabaseService;
import com.autoscolombia.customer.service.CustomerDomainService;
import com.autoscolombia.customer.service.domain.Customer;
import com.autoscolombia.parkingsession.persistence.ParkingSessionDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerDomainServiceImpl implements CustomerDomainService {

    private final CustomerDatabaseService customerDatabaseService;
    private final ParkingSessionDatabaseService parkingSessionDatabaseService;

    @Override
    public Customer create(Customer customer) {
        Customer normalized = normalize(customer);
        validateUniqueness(normalized, null);
        return customerDatabaseService.save(normalized.toBuilder().id(null).build());
    }

    @Override
    public Customer update(Long id, Customer customer) {
        getById(id);
        Customer normalized = normalize(customer);
        validateUniqueness(normalized, id);
        return customerDatabaseService.save(normalized.toBuilder().id(id).build());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> search(String search, Pageable pageable) {
        return customerDatabaseService.search(search == null ? "" : search.trim(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getById(Long id) {
        return customerDatabaseService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el cliente con id " + id + "."));
    }

    @Override
    public void delete(Long id) {
        getById(id);
        if (parkingSessionDatabaseService.existsOpenByCustomerId(id)) {
            throw new BusinessRuleException(
                    "No se puede eliminar el cliente: tiene un vehículo con una estancia abierta.");
        }
        if (parkingSessionDatabaseService.existsAnyByCustomerId(id)) {
            throw new BusinessRuleException(
                    "No se puede eliminar el cliente: sus vehículos tienen historial de estancias.");
        }
        customerDatabaseService.deleteById(id);
    }

    private void validateUniqueness(Customer customer, Long excludeId) {
        if (customerDatabaseService.existsByIdentification(
                customer.getIdentificationType(), customer.getIdentificationNumber(), excludeId)) {
            throw new BusinessRuleException("Ya existe un cliente con la identificación "
                    + customer.getIdentificationType() + " " + customer.getIdentificationNumber() + ".");
        }
        if (customer.getEmail() != null && customerDatabaseService.existsByEmail(customer.getEmail(), excludeId)) {
            throw new BusinessRuleException("Ya existe un cliente con el correo " + customer.getEmail() + ".");
        }
    }

    private Customer normalize(Customer customer) {
        return customer.toBuilder()
                .identificationType(customer.getIdentificationType().trim().toUpperCase(Locale.ROOT))
                .identificationNumber(customer.getIdentificationNumber().trim())
                .name(customer.getName().trim())
                .email(blankToNull(customer.getEmail()))
                .phone(blankToNull(customer.getPhone()))
                .build();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
