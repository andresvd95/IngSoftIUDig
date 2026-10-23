package com.autoscolombia.parkingsession.controller.dto;

import com.autoscolombia.operator.controller.dto.OperatorSummaryViewApi;
import com.autoscolombia.parkingspot.controller.dto.ParkingSpotSummaryViewApi;
import com.autoscolombia.vehicle.controller.dto.VehicleSummaryViewApi;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public record ParkingSessionViewApi(
        Long id,
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        VehicleSummaryViewApi vehicle,
        ParkingSpotSummaryViewApi spot,
        OperatorSummaryViewApi entryOperator,
        OperatorSummaryViewApi exitOperator) implements Serializable {

    @Serial
    private static final long serialVersionUID = 409006287147962998L;
}