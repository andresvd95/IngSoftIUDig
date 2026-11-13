package com.autoscolombia.customer.persistence;

import com.autoscolombia.customer.service.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerDatabaseService {

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    Page<Customer> search(String search, Pageable pageable);

    boolean existsByIdentification(String type, String number, Long excludeId);

    boolean existsByEmail(String email, Long excludeId);

    void deleteById(Long id);
}
