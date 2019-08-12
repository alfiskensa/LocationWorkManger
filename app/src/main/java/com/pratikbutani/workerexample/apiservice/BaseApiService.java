package com.pratikbutani.workerexample.apiservice;

import com.pratikbutani.workerexample.model.LocationHistory;
import com.pratikbutani.workerexample.model.LocationHistoryResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BaseApiService {

    @POST("api/locationhistory")
    Observable<LocationHistoryResponse> locationSend(@Body LocationHistory locationHistory);
}