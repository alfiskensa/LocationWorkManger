package com.pratikbutani.workerexample.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class LocationHistoryResponse {
    private LocationHistory locationHistory;
    private String message;
}
