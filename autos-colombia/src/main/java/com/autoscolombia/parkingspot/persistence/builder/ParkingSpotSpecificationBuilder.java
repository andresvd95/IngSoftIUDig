package com.autoscolombia.parkingspot.persistence.builder;

import com.autoscolombia.common.persistence.builder.SpecificationBuilder;
import com.autoscolombia.parkingspot.persistence.entity.ParkingSpotEntity;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.function.Supplier;

@Builder
public class ParkingSpotSpecificationBuilder extends SpecificationBuilder<ParkingSpotEntity> {

    private Boolean isAvailable;

    @Override
    protected Map<Supplier<Object>, Specification<ParkingSpotEntity>> getSpecificationMapping() {

        return Map.of(
                () -> isAvailable, ((root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get("isAvailable"), isAvailable))
        );
    }
}
