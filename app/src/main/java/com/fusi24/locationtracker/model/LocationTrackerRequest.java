package com.fusi24.locationtracker.model;

import android.location.Location;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
//@Entity(tableName = "locationTrackerRequestTbl")
public class LocationTrackerRequest {

    //@PrimaryKey(autoGenerate = true)
    //private Integer id;

    private Location location;

    private Boolean isActive;
}
