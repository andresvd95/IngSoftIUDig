package com.autoscolombia.customer.persistence.repository;

import com.autoscolombia.customer.persistence.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    @Query("select c from CustomerEntity c where lower(c.name) like lower(concat('%', :search, '%')) "
            + "or lower(c.identificationNumber) like lower(concat('%', :search, '%'))")
    Page<CustomerEntity> search(@Param("search") String search, Pageable pageable);

    boolean existsByIdentificationTypeIgnoreCaseAndIdentificationNumberIgnoreCase(String type, String number);

    boolean existsByIdentificationTypeIgnoreCaseAndIdentificationNumberIgnoreCaseAndIdNot(
            String type, String number, Long id);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
