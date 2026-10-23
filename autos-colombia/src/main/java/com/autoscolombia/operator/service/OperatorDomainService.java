package com.autoscolombia.operator.service;

import com.autoscolombia.operator.service.domain.Operator;

import java.util.Optional;

public interface OperatorDomainService {

    Optional<Operator> findById(Long id);
}
