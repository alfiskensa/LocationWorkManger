package com.fusi24.locationtracker.model.jsonapi;

import lombok.Getter;
import lombok.Setter;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "statusSid")
@Getter @Setter
public class StatusSid extends Resource {

    private String name;
    private String description;
    private String filter;
}
