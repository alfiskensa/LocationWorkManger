package com.fusi24.locationtracker.apiservice;

public class ApiUtils {

    private static final String BASE_URL_API = "http://35.232.163.26/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
