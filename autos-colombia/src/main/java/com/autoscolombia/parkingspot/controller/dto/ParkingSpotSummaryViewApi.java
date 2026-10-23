package com.autoscolombia.parkingspot.controller.dto;

import java.io.Serial;
import java.io.Serializable;

public record ParkingSpotSummaryViewApi(
        Long id,
        String spotNumber) implements Serializable {

    @Serial
    private static final long serialVersionUID = 4009076532781763468L;
}