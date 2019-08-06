package com.pratikbutani.workerexample.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


@Entity(tableName = "locationTbl")
public class LocationHistory {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Double longitude;

    private Double latitude;

    private Date date;

    private String deviceInfo;

    private String message;

    private String employeeId;

    public Integer checkpointId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(Integer checkpointId) {
        this.checkpointId = checkpointId;
    }

    @Override
    public String toString() {
        return "LocationHistory{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", date=" + date +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", message='" + message + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", checkpointId=" + checkpointId +
                '}';
    }
}
