package com.fusi24.locationtracker.apiservice;

import com.fusi24.locationtracker.model.jsonapi.Employee;
import com.fusi24.locationtracker.model.jsonapi.Location;
import com.fusi24.locationtracker.model.jsonapi.LocationHistory;
import com.fusi24.locationtracker.model.jsonapi.LocationType;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import moe.banana.jsonapi2.JsonApiConverterFactory;
import moe.banana.jsonapi2.ResourceAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getJsonApiClient(String baseUrl){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .readTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        JsonAdapter.Factory factory = ResourceAdapterFactory.builder()
                .add(Location.class)
                .add(LocationType.class)
                .add(Employee.class)
                .add(LocationHistory.class)
                .build();

        Moshi moshi = new Moshi.Builder()
                .add(factory)
                .add(Date.class, new Rfc3339DateJsonAdapter().nullSafe())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JsonApiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public static Retrofit getBcui2Client(String baseUrl){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .readTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        JsonAdapter.Factory factory = ResourceAdapterFactory.builder()
                .add(Location.class)
                .add(LocationType.class)
                .add(Employee.class)
                .add(LocationHistory.class)
                .build();

        Moshi moshi = new Moshi.Builder()
                .add(factory)
                .add(Date.class, new Rfc3339DateJsonAdapter().nullSafe())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JsonApiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
}
