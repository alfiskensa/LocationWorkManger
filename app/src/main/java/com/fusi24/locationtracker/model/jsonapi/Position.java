package com.fusi24.locationtracker.model.jsonapi;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "position")
@Getter @Setter
public class Position extends Resource {

    private String name;
    private String description;
    private HasOne<PositionType> positionType;
    private Date updateDate;
    private HasOne<PositionType> positionCategory;
    private HasOne<Company> company;
}
