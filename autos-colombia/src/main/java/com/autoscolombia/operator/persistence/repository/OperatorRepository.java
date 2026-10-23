package com.autoscolombia.operator.persistence.repository;

import com.autoscolombia.operator.persistence.entity.OperatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepository extends JpaRepository<OperatorEntity, Long> {
}
