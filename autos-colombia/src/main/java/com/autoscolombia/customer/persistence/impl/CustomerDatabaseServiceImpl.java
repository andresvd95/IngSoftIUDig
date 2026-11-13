package com.autoscolombia.customer.persistence.impl;

import com.autoscolombia.customer.persistence.CustomerDatabaseService;
import com.autoscolombia.customer.persistence.entity.CustomerEntity;
import com.autoscolombia.customer.persistence.mapper.CustomerMapper;
import com.autoscolombia.customer.persistence.repository.CustomerRepository;
import com.autoscolombia.customer.service.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerDatabaseServiceImpl implements CustomerDatabaseService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = customer.getId() == null
                ? mapper.mapToEntity(customer)
                : mapper.mapToExistingEntity(customer, customerRepository.findById(customer.getId()).orElseThrow());
        return mapper.mapToDomain(customerRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id).map(mapper::mapToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> search(String search, Pageable pageable) {
        return customerRepository.search(search, pageable).map(mapper::mapToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdentification(String type, String number, Long excludeId) {
        return excludeId == null
                ? customerRepository.existsByIdentificationTypeIgnoreCaseAndIdentificationNumberIgnoreCase(type, number)
                : customerRepository.existsByIdentificationTypeIgnoreCaseAndIdentificationNumberIgnoreCaseAndIdNot(
                        type, number, excludeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email, Long excludeId) {
        return excludeId == null
                ? customerRepository.existsByEmailIgnoreCase(email)
                : customerRepository.existsByEmailIgnoreCaseAndIdNot(email, excludeId);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
        customerRepository.flush();
    }
}
