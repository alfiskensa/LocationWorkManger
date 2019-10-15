package com.fusi24.locationtracker.model.jsonapi;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "company")
@Getter @Setter
public class Company extends Resource {

    private String code;
    private String name;
    private String address;
    private String cityAddress;
    private String postalCodeAddress;
    private String countryTelephoneCode;
    private String prefixTelephone;
    private String telephone;
    private String email;
    private String taxIdNumber;
    private HasOne<StatusSid> sidStatus;
    private Date updateDate;
    private String urlDocument;
    private String urlCode;
    private String companyCode;
    private HasOne<EmployeeType> employeeType;

}
