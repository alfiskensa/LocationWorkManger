package com.fusi24.locationtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.fusi24.locationtracker.adapter.LocationHistoryAdapter;
import com.fusi24.locationtracker.apiservice.ApiUtils;
import com.fusi24.locationtracker.apiservice.BaseJsonApiService;
import com.fusi24.locationtracker.databinding.ActivityHistoryBinding;
import com.fusi24.locationtracker.databinding.ActivityMainBinding;
import com.fusi24.locationtracker.model.jsonapi.Location;
import com.fusi24.locationtracker.model.jsonapi.LocationHistory;
import com.fusi24.locationtracker.util.JsonApiExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HistoryActivity extends AppCompatActivity {

    private BaseJsonApiService moe;

    ActivityHistoryBinding activityHistoryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_history);
        moe = ApiUtils.getJsonAPIService();
        activityHistoryBinding.historyRecyclerView.setHasFixedSize(true);
        activityHistoryBinding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getLocationHistory();
    }

    private void getLocationHistory(){

        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");

        String firstDate = "2019-09-12 00:00:00";
        String lastDate = "2019-09-12 23:59:59";

        Date startDate = parseDate(firstDate);
        Date endDate = parseDate(lastDate);

        String sd = ymd.format(startDate)+"T"+hms.format(startDate);
        String ed = ymd.format(endDate)+"T"+hms.format(endDate);

//        System.out.println("start date: "+sdf.format(startDate));
//        System.out.println("end date: "+sdf.format(endDate));

//        LocalDateTime ldt = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
//        System.out.println("local date time "+ldt.toString());

//        System.out.println("start local date"+toLocalDate(startDate).toString());
//        System.out.println("end local date"+toLocalDate(endDate).toString());
        String includes = "location.parent.parent,location.type,employee";
        moe.getLocationHistoryByEmployeeIdAndDateAndIsOnSite("43884",sd,ed,true,includes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LocationHistory>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @SuppressLint("NewApi")
                    @Override
                    public void onNext(List<LocationHistory> locationHistories) {
//                        Gson gson = new GsonBuilder().setExclusionStrategies(new JsonApiExclusionStrategy()).create();
//                        locationHistories.forEach(event -> {
//                            if(event.getLocation().getLocationType().getId().equals("100")){
//                                System.out.println("location: "+event.getLocation());
//                                System.out.println("anone: "+gson.toJson(event));
//                            }
//
//                        });
                       setRecyclerView(locationHistories);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setRecyclerView(List<LocationHistory> list){
        LocationHistoryAdapter adapter = new LocationHistoryAdapter(list);
        System.out.println("kawaiii: "+adapter.getItemCount());
        activityHistoryBinding.historyRecyclerView.setAdapter(adapter);
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
