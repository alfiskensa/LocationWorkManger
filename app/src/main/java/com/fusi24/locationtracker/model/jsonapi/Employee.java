package com.fusi24.locationtracker.model.jsonapi;

import androidx.annotation.Keep;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

@JsonApi(type = "employee")
@Keep
@Getter @Setter
public class Employee extends Resource implements Serializable {

    private String code;
    private String sidCode;
    private String regNumEmployee;
    private HasOne<Company> company;
    private String name;
    private Date dateOfBirth;
    private String placeOfBirth;
    private HasOne<Position> structuralPosition;
    private HasOne<Position> functionalPosition;
    private HasOne<Position> reservedPosition;
    private Date updateDate;
    private HasOne<StatusSid> status;
    private String address;
    private String districtAddress;
    private String provinceAddress;
    private String country;
    private String phone;
    private String emergencyPhone;
    private String emergencyContactPerson;
    private String description;
    private HasOne<EmployeeType> employeeType;
    private String email;
    private String urlPhoto;
    private String urlCode;
    private Date firstDayDate;
    private Date lastDayDate;
    private String cityAddress;
    private String postalCodeAddress;
    private String neighborhoodAssociationAddress;
    private String hamletAssociationAddress;
    private String villageAddress;
    private String subDistrictAddress;
    private HasOne<Department> department;

    public Employee(){

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSidCode() {
        return sidCode;
    }

    public void setSidCode(String sidCode) {
        this.sidCode = sidCode;
    }

    public String getRegNumEmployee() {
        return regNumEmployee;
    }

    public void setRegNumEmployee(String regNumEmployee) {
        this.regNumEmployee = regNumEmployee;
    }

    public Company getCompany() {
        return company.get(getDocument());
    }

    public void setCompany(HasOne<Company> company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public HasOne<Position> getStructuralPosition() {
        return structuralPosition;
    }

    public void setStructuralPosition(HasOne<Position> structuralPosition) {
        this.structuralPosition = structuralPosition;
    }

    public HasOne<Position> getFunctionalPosition() {
        return functionalPosition;
    }

    public void setFunctionalPosition(HasOne<Position> functionalPosition) {
        this.functionalPosition = functionalPosition;
    }

    public HasOne<Position> getReservedPosition() {
        return reservedPosition;
    }

    public void setReservedPosition(HasOne<Position> reservedPosition) {
        this.reservedPosition = reservedPosition;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public HasOne<StatusSid> getStatus() {
        return status;
    }

    public void setStatus(HasOne<StatusSid> status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrictAddress() {
        return districtAddress;
    }

    public void setDistrictAddress(String districtAddress) {
        this.districtAddress = districtAddress;
    }

    public String getProvinceAddress() {
        return provinceAddress;
    }

    public void setProvinceAddress(String provinceAddress) {
        this.provinceAddress = provinceAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getEmergencyContactPerson() {
        return emergencyContactPerson;
    }

    public void setEmergencyContactPerson(String emergencyContactPerson) {
        this.emergencyContactPerson = emergencyContactPerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HasOne<EmployeeType> getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(HasOne<EmployeeType> employeeType) {
        this.employeeType = employeeType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getUrlCode() {
        return urlCode;
    }

    public void setUrlCode(String urlCode) {
        this.urlCode = urlCode;
    }

    public Date getFirstDayDate() {
        return firstDayDate;
    }

    public void setFirstDayDate(Date firstDayDate) {
        this.firstDayDate = firstDayDate;
    }

    public Date getLastDayDate() {
        return lastDayDate;
    }

    public void setLastDayDate(Date lastDayDate) {
        this.lastDayDate = lastDayDate;
    }

    public String getCityAddress() {
        return cityAddress;
    }

    public void setCityAddress(String cityAddress) {
        this.cityAddress = cityAddress;
    }

    public String getPostalCodeAddress() {
        return postalCodeAddress;
    }

    public void setPostalCodeAddress(String postalCodeAddress) {
        this.postalCodeAddress = postalCodeAddress;
    }

    public String getNeighborhoodAssociationAddress() {
        return neighborhoodAssociationAddress;
    }

    public void setNeighborhoodAssociationAddress(String neighborhoodAssociationAddress) {
        this.neighborhoodAssociationAddress = neighborhoodAssociationAddress;
    }

    public String getHamletAssociationAddress() {
        return hamletAssociationAddress;
    }

    public void setHamletAssociationAddress(String hamletAssociationAddress) {
        this.hamletAssociationAddress = hamletAssociationAddress;
    }

    public String getVillageAddress() {
        return villageAddress;
    }

    public void setVillageAddress(String villageAddress) {
        this.villageAddress = villageAddress;
    }

    public String getSubDistrictAddress() {
        return subDistrictAddress;
    }

    public void setSubDistrictAddress(String subDistrictAddress) {
        this.subDistrictAddress = subDistrictAddress;
    }

    public HasOne<Department> getDepartment() {
        return department;
    }

    public void setDepartment(HasOne<Department> department) {
        this.department = department;
    }

}
