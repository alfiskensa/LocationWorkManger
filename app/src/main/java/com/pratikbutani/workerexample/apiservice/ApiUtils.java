package com.pratikbutani.workerexample.apiservice;

public class ApiUtils {

    private static final String BASE_URL_API = "http://192.168.12.129:40000/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
