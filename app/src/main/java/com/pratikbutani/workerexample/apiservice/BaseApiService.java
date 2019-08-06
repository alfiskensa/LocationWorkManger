package com.pratikbutani.workerexample.apiservice;

import com.pratikbutani.workerexample.model.LocationHistory;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BaseApiService {

    @POST("api/locationHistory")
    Observable<LocationHistory> locationSend(@Body LocationHistory locationHistory);
}
