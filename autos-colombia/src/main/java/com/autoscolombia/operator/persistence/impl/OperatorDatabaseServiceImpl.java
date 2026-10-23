package com.autoscolombia.operator.persistence.impl;

import com.autoscolombia.operator.persistence.OperatorDatabaseService;
import com.autoscolombia.operator.persistence.mapper.OperatorMapper;
import com.autoscolombia.operator.persistence.repository.OperatorRepository;
import com.autoscolombia.operator.service.domain.Operator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OperatorDatabaseServiceImpl implements OperatorDatabaseService {

    private final OperatorRepository operatorRepository;
    private final OperatorMapper mapper;

    @Override
    public Optional<Operator> findById(Long id) {
        return operatorRepository.findById(id)
                .map(mapper::mapToDomain);
    }
}
