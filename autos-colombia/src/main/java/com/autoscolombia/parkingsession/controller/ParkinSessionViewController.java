package com.autoscolombia.parkingsession.controller;

import com.autoscolombia.parkingsession.controller.dto.ParkingSessionViewApi;
import com.autoscolombia.parkingsession.controller.mapper.ParkingSessionApiMapper;
import com.autoscolombia.parkingsession.service.ParkingSessionDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("parking-session")
@RequiredArgsConstructor
public class ParkinSessionViewController {

    private final ParkingSessionDomainService parkingSessionDomainService;
    private final ParkingSessionApiMapper apiMapper;

    @GetMapping
    public Page<ParkingSessionViewApi> getAll(
            @PageableDefault(size = 20, sort = "entryTime", direction = Sort.Direction.DESC) Pageable pageable) {

        return parkingSessionDomainService.findAll(pageable)
                .map(apiMapper::mapToViewApi);
    }

    /** Vehiculos actualmente dentro del parqueadero (estancias sin salida). */
    @GetMapping("active")
    public List<ParkingSessionViewApi> getActive() {

        return parkingSessionDomainService.findActive().stream()
                .map(apiMapper::mapToViewApi)
                .toList();
    }

    /** Historial de estancias cerradas, la salida mas reciente primero. */
    @GetMapping("history")
    public Page<ParkingSessionViewApi> getHistory(
            @PageableDefault(size = 50, sort = "exitTime", direction = Sort.Direction.DESC) Pageable pageable) {

        return parkingSessionDomainService.findHistory(pageable)
                .map(apiMapper::mapToViewApi);
    }
}
