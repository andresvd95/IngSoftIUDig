package com.autoscolombia.parkingspot.controller;

import com.autoscolombia.parkingspot.controller.dto.ParkingSpotCreationApi;
import com.autoscolombia.parkingspot.controller.dto.ParkingSpotViewApi;
import com.autoscolombia.parkingspot.controller.mapper.ParkingSpotApiMapper;
import com.autoscolombia.parkingspot.service.ParkingSpotDomainService;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.parkingspot.service.domain.ParkingSpotQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("parking-spot")
@RequiredArgsConstructor
public class ParkingSpotCRUDController {

    private final ParkingSpotDomainService parkingSpotDomainService;
    private final ParkingSpotApiMapper apiMapper;

    @PostMapping
    public ParkingSpotViewApi createOrUpdate(@RequestBody ParkingSpotCreationApi creationApi) {

        ParkingSpot spotToCreate = apiMapper.mapToDomain(creationApi);
        return apiMapper.mapToViewApi(parkingSpotDomainService.createOrUpdate(spotToCreate));
    }

    @GetMapping
    public Page<ParkingSpotViewApi> getParkingSpots(@PageableDefault(size = 20) Pageable pageable,
                                                    ParkingSpotQuery query) {

        return parkingSpotDomainService.findAll(pageable, query)
                .map(apiMapper::mapToViewApi);
    }
}
