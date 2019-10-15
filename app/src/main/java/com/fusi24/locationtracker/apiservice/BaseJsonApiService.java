package com.fusi24.locationtracker.apiservice;

import com.fusi24.locationtracker.model.jsonapi.Location;
import com.fusi24.locationtracker.model.jsonapi.LocationHistory;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseJsonApiService {

    @GET("locations/{id}")
    Observable<Location> getLocationById(@Path("id") String id);

    @GET("locations")
    Observable<Location> getLocationByParentId(@Query("filter[parent.id]") String parentId);

    @GET("locations")
    Observable<List<Location>> getLocationByTypeId(@Query("filter[type.id]") String typeId, @Query("include") String includes);

    @GET("locationHistory")
    Observable<LocationHistory> getLocationHistoryByEmployeeId(@Query("filter[employee.id]") String employeeId);

    @GET("locationHistory")
    Observable<List<LocationHistory>> getLocationHistoryByEmployeeIdAndDate(@Query("filter[employee.id]") String employeeId,
                                                                      @Query("filter[date][GE]") String startDate,
                                                                      @Query("filter[date][LE]") String endDate,
                                                                      @Query("include") String includes);

    @GET("locationHistory")
    Observable<List<LocationHistory>> getLocationHistoryByEmployeeIdAndDateAndIsOnSite(@Query("filter[employee.id]") String employeeId,
                                                                            @Query("filter[date][GE]") String startDate,
                                                                            @Query("filter[date][LE]") String endDate,
                                                                            @Query("filter[isOnSite]") Boolean isOnSite,
                                                                            @Query("include") String includes);


}
