package com.autoscolombia.parkingspot.controller;

import com.autoscolombia.parkingspot.controller.dto.ParkingSpotCreationApi;
import com.autoscolombia.parkingspot.controller.dto.ParkingSpotViewApi;
import com.autoscolombia.parkingspot.controller.mapper.ParkingSpotApiMapper;
import com.autoscolombia.parkingspot.service.ParkingSpotDomainService;
import com.autoscolombia.parkingspot.service.domain.ParkingSpot;
import com.autoscolombia.parkingspot.service.domain.ParkingSpotQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("parking-spot")
@RequiredArgsConstructor
public class ParkingSpotCRUDController {

    private final ParkingSpotDomainService parkingSpotDomainService;
    private final ParkingSpotApiMapper apiMapper;

    /** POST /parking-spot : crea una celda disponible (409 si el numero ya existe). */
    @PostMapping
    public ParkingSpotViewApi create(@Valid @RequestBody ParkingSpotCreationApi creationApi) {

        ParkingSpot spotToCreate = apiMapper.mapToDomain(creationApi);
        return apiMapper.mapToViewApi(parkingSpotDomainService.create(spotToCreate));
    }

    /** PUT /parking-spot/{id} : edita numero y tipo (409 duplicada u ocupada, 404 inexistente). */
    @PutMapping("/{id}")
    public ParkingSpotViewApi update(@PathVariable Long id, @Valid @RequestBody ParkingSpotCreationApi creationApi) {

        ParkingSpot changes = apiMapper.mapToDomain(creationApi);
        return apiMapper.mapToViewApi(parkingSpotDomainService.update(id, changes));
    }

    /** DELETE /parking-spot/{id} : 204; 409 si esta ocupada o tiene historial; 404 si no existe. */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        parkingSpotDomainService.delete(id);
    }

    @GetMapping
    public Page<ParkingSpotViewApi> getParkingSpots(@PageableDefault(size = 20) Pageable pageable,
                                                    ParkingSpotQuery query) {

        return parkingSpotDomainService.findAll(pageable, query)
                .map(apiMapper::mapToViewApi);
    }
}
