package com.autoscolombia.operator.persistence;

import com.autoscolombia.operator.service.domain.Operator;

import java.util.Optional;

public interface OperatorDatabaseService {

    Optional<Operator> findById(Long id);
}
