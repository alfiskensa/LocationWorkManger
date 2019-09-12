package com.fusi24.locationtracker.apiservice;

import com.fusi24.locationtracker.model.LocationHistory;
import com.fusi24.locationtracker.model.LocationHistoryResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BaseApiService {

    @POST("api/locationhistory/locations")
    Observable<LocationHistoryResponse> locationSend(@Body LocationHistory locationHistory);
}