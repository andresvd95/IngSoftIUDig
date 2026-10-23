package com.autoscolombia.common.persistence.builder;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.function.Supplier;

public abstract class SpecificationBuilder<T> {

    public Specification<T> buildSpecification() {
        return getSpecificationMapping().entrySet().stream()
                .filter(entry -> ObjectUtils.isNotEmpty(entry.getKey().get()))
                .map(Map.Entry::getValue)
                .reduce(Specification::and)
                .orElse(null);

    }

    protected abstract Map<Supplier<Object>, Specification<T>> getSpecificationMapping();
}