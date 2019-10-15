package com.fusi24.locationtracker.model.jsonapi;

import com.squareup.moshi.Json;

import lombok.Getter;
import lombok.Setter;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "locations")
public class Location extends Resource {

    private HasOne<Location> parent;

    @Json(name = "type")
    private HasOne<LocationType> locationType;

    private String name;

    private String shortName;

    private Boolean isActive;

    private Double centerLatitude;

    private Double centerLongitude;

    public Location getParent() {
        return parent.get(getDocument());
    }

    public LocationType getLocationType() {
        return locationType.get(getDocument());
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Double getCenterLatitude() {
        return centerLatitude;
    }

    public Double getCenterLongitude() {
        return centerLongitude;
    }

    public void setParent(HasOne<Location> parent) {
        this.parent = parent;
    }

    public void setLocationType(HasOne<LocationType> locationType) {
        this.locationType = locationType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setCenterLatitude(Double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public void setCenterLongitude(Double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }
}
