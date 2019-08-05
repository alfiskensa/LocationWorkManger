package com.pratikbutani.workerexample.model;

import java.math.BigInteger;
import java.util.Date;


public class LocationSender {

    private BigInteger id;

    private Double centerLongitude;

    private Double centerLatitude;

    private Date date;

    private String deviceInfo;

    private String message;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Double getCenterLongitude() {
        return centerLongitude;
    }

    public void setCenterLongitude(Double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public Double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLatitude(Double centerLatitude) {
        this.centerLatitude = centerLatitude;
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

    public String getMassage() {
        return message;
    }

    public void setMassage(String massage) {
        this.message = massage;
    }
}
