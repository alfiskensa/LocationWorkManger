package com.fusi24.locationtracker.model;

import com.fusi24.locationtracker.model.jsonapi.Employee;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.Map;

import lombok.ToString;

@ToString
public class EmployeeClusterItem implements ClusterItem {

    private LatLng mPosition;

    private Map<String, Object> employee;

    public EmployeeClusterItem(LatLng mPosition, Map<String, Object> employee) {
        this.mPosition = mPosition;
        this.employee = employee;
    }

    public Map<String, Object> getEmployee() {
        return employee;
    }

    public void setEmployee(Map<String, Object> employee) {
        this.employee = employee;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    public void setPosition(LatLng mPosition){
        this.mPosition = mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
