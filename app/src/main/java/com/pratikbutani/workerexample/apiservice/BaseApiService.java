package com.pratikbutani.workerexample.apiservice;

import com.pratikbutani.workerexample.model.LocationSender;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BaseApiService {

    @POST("api/locationReceiver")
    Observable<LocationSender> locationSend(@Body LocationSender locationSender);
}
