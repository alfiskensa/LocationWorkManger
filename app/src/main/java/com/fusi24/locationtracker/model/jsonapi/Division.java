package com.fusi24.locationtracker.model.jsonapi;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "division")
@Getter @Setter
public class Division extends Resource {
    private String name;
    private HasOne<Company> company;
    private HasOne<StatusSid> status;
    private Date updateDate;
}
