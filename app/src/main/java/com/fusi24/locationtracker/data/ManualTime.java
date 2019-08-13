package com.fusi24.locationtracker.data;

import lombok.Data;

@Data
public class ManualTime {
    private Long seconds;
    private Long minutes;
    private Long hours;
    private Long days;
}
