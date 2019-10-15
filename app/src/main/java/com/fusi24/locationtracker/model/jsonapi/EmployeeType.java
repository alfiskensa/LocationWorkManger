package com.fusi24.locationtracker.model.jsonapi;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "employeeType")
@Getter @Setter
public class EmployeeType extends Resource {
    private String name;
    private String description;
    private Date updateDate;
}
