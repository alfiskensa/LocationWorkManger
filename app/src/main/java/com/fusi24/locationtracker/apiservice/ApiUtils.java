package com.fusi24.locationtracker.apiservice;

public class ApiUtils {

    private static final String BASE_URL_API = "http://35.232.163.26/";

    private static final String BASE_URL_JSON_API = "http://35.232.163.26/api/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }

    public static BaseJsonApiService getJsonAPIService(){
        return RetrofitClient.getJsonApiClient(BASE_URL_JSON_API).create(BaseJsonApiService.class);
    }
}
