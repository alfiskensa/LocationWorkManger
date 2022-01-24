package com.pratikbutani.workerexample.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class LocationHistoryResponse {
    private LocationHistory locationHistory;
    private String message;

    public LocationHistory getLocationHistory() {
        return locationHistory;
    }

    public void setLocationHistory(LocationHistory locationHistory) {
        this.locationHistory = locationHistory;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
