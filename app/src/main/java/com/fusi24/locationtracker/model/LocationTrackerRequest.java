package com.fusi24.locationtracker.model;

import android.location.Location;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fusi24.locationtracker.model.jsonapi.Employee;
import com.google.gson.JsonElement;

import java.util.Map;

import lombok.Data;

@Data
//@Entity(tableName = "locationTrackerRequestTbl")
public class LocationTrackerRequest {

    //@PrimaryKey(autoGenerate = true)
    //private Integer id;
    public Object employee;

    public Location location;

    public Boolean isActive;
}
