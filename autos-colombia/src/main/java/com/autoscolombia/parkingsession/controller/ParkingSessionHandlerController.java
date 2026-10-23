package com.autoscolombia.parkingsession.controller;

import com.autoscolombia.parkingsession.controller.dto.ParkingSessionExitCreationApi;
import com.autoscolombia.parkingsession.controller.dto.ParkingSessionIngressCreationApi;
import com.autoscolombia.parkingsession.controller.dto.ParkingSessionViewApi;
import com.autoscolombia.parkingsession.controller.mapper.ParkingSessionApiMapper;
import com.autoscolombia.parkingsession.handler.ParkingSessionRegisterEntryHandler;
import com.autoscolombia.parkingsession.handler.ParkingSessionRegisterExitHandler;
import com.autoscolombia.parkingsession.service.domain.ParkingSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("parking-session")
@RequiredArgsConstructor
public class ParkingSessionHandlerController {

    private final ParkingSessionRegisterEntryHandler parkingSessionRegisterEntryHandler;
    private final ParkingSessionRegisterExitHandler parkingSessionRegisterExitHandler;
    private final ParkingSessionApiMapper apiMapper;

    @PostMapping("vehicle-entry")
    public ParkingSessionViewApi registerVehicleEntry(@RequestBody ParkingSessionIngressCreationApi creationApi) {

        ParkingSession parkingSessionToSave = apiMapper.mapToDomain(creationApi);
        return apiMapper.mapToViewApi(parkingSessionRegisterEntryHandler.registerEntry(parkingSessionToSave));
    }

    @PostMapping("vehicle-exit")
    public ParkingSessionViewApi registerVehicleExit(@RequestBody ParkingSessionExitCreationApi creationApi) {

        ParkingSession parkingSessionToSave = apiMapper.mapToDomain(creationApi);
        return apiMapper.mapToViewApi(parkingSessionRegisterExitHandler.registerExit(parkingSessionToSave));
    }
}
