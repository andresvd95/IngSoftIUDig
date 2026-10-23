package com.autoscolombia.operator.service.impl;

import com.autoscolombia.operator.persistence.OperatorDatabaseService;
import com.autoscolombia.operator.service.OperatorDomainService;
import com.autoscolombia.operator.service.domain.Operator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OperatorDomainServiceImpl implements OperatorDomainService {

    private final OperatorDatabaseService operatorDatabaseService;

    @Override
    public Optional<Operator> findById(Long id) {
        return operatorDatabaseService.findById(id);
    }
}
