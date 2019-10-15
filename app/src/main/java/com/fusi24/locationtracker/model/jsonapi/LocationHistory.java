package com.fusi24.locationtracker.model.jsonapi;

import java.io.Serializable;
import java.util.Date;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "locationHistory")
public class LocationHistory extends Resource implements Serializable {

    private Double longitude;

    private Double latitude;

    private Date date;

    private String deviceInfo;

    private HasOne<Employee> employee;

    private HasOne<Location> location;

    private HasOne<LocationHistory> checkpoint;

    private Boolean isOnSite;

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

    public Employee getEmployee() {
        return employee.get(getDocument());
    }

    public void setEmployee(HasOne<Employee> employee) {
        this.employee = employee;
    }

    public Location getLocation() {
        return location.get(getDocument());
    }

    public void setLocation(HasOne<Location> location) {
        this.location = location;
    }

    public LocationHistory getCheckpoint() {
        return checkpoint.get(getDocument());
    }

    public void setCheckpoint(HasOne<LocationHistory> checkpoint) {
        this.checkpoint = checkpoint;
    }

    public Boolean getOnSite() {
        return isOnSite;
    }

    public void setOnSite(Boolean onSite) {
        isOnSite = onSite;
    }
}
