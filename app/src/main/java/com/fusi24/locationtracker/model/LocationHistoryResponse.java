package com.fusi24.locationtracker.model;

import lombok.Data;

@Data
public class LocationHistoryResponse {
    private LocationHistory locationHistory;
    private String message;
}
