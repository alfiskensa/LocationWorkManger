package com.fusi24.locationtracker.apiservice;

public class ApiUtils {

    private static final String BASE_URL_API = "http://35.232.163.26/";

    private static final String BASE_URL_JSON_API = "http://35.232.163.26/api/";

    private static final String BASE_URL_BCUI2 = "http://bcui2.fusi24.com/beats2/api/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }

    public static BaseJsonApiService getJsonAPIService(){
        return RetrofitClient.getJsonApiClient(BASE_URL_JSON_API).create(BaseJsonApiService.class);
    }

    public static BaseBCUI2Service getBCUI2Service(){
        return RetrofitClient.getBcui2Client(BASE_URL_BCUI2).create(BaseBCUI2Service.class);
    }
}
