package com.autoscolombia.parkingsession.controller;

import com.autoscolombia.parkingsession.controller.dto.ParkingSessionViewApi;
import com.autoscolombia.parkingsession.controller.mapper.ParkingSessionApiMapper;
import com.autoscolombia.parkingsession.service.ParkingSessionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("parking-session")
@RequiredArgsConstructor
public class ParkinSessionViewController {

    private final ParkingSessionDomainService parkingSessionDomainService;
    private final ParkingSessionApiMapper apiMapper;

    @GetMapping
    public Page<ParkingSessionViewApi> getAll(@PageableDefault(size = 20) Pageable pageable) {

        return parkingSessionDomainService.findAll(pageable)
                .map(apiMapper::mapToViewApi);
    }
}
