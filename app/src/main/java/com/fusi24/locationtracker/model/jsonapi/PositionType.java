package com.fusi24.locationtracker.model.jsonapi;

import com.squareup.moshi.Json;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "positionType")
@Getter @Setter
public class PositionType extends Resource {
    private String name;
    private String description;
    private Date updateDate;
    private String type;
}
