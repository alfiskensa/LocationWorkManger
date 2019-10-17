package com.fusi24.locationtracker.apiservice;

import com.fusi24.locationtracker.model.jsonapi.Employee;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BaseBCUI2Service {

    @GET("employee/{id}")
    Observable<Employee> findById(@Path("id") String id);
}
