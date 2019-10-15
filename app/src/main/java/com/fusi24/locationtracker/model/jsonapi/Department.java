package com.fusi24.locationtracker.model.jsonapi;

import lombok.Getter;
import lombok.Setter;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "department")
@Getter @Setter
public class Department extends Resource {

    private String name;
    private HasOne<Company> company;
    private HasOne<Department> parent;
    private Boolean isActive;
    private HasOne<Division> division;
}
