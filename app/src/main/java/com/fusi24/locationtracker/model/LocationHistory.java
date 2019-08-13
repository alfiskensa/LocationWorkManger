package com.fusi24.locationtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Data;


@Data
@Entity(tableName = "locationTbl")
public class LocationHistory {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Double longitude;

    private Double latitude;

    private Date date;

    private String deviceInfo;

    private Integer employeeId;

    public Integer checkpointId;

}
