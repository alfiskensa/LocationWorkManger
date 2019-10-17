package com.fusi24.locationtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.fusi24.locationtracker.apiservice.ApiUtils;
import com.fusi24.locationtracker.apiservice.BaseJsonApiService;
import com.fusi24.locationtracker.data.AppDatabase;
import com.fusi24.locationtracker.databinding.ActivityMainBinding;
import com.fusi24.locationtracker.maps.MapsActivity;
import com.fusi24.locationtracker.model.LocationTrackerRequest;
import com.fusi24.locationtracker.service.MyWorker;
import com.fusi24.locationtracker.service.TrackerService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;

    private static final String TAG = "BackgroundLocationUpdate";

    private ActivityMainBinding mainBinding;
    private DatabaseReference ref;
    private AppDatabase db;
    private String msg;
    private BaseJsonApiService moe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if(msg != null){
            mainBinding.showMap.setVisibility(View.VISIBLE);
        }else{
            mainBinding.showMap.setVisibility(View.INVISIBLE);
        }

        db = AppDatabase.getInstance(getApplicationContext());

        moe = ApiUtils.getJsonAPIService();

        final String path = getString(R.string.firebase_path) + "/" + getString(R.string.tracker_id);
        ref = FirebaseDatabase.getInstance().getReference(path);

        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

        startWorker();

        mainBinding.appCompatButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainBinding.appCompatButtonStart.getText().toString().equalsIgnoreCase(getString(R.string.button_text_start))){
                    Intent intent = new Intent(MainActivity.this, TrackerService.class);
                    intent.setAction(TrackerService.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent);
                    stopWorker();
                    mainBinding.appCompatButtonStart.setText(getString(R.string.button_text_stop));
                    mainBinding.message.setText(R.string.message_worker_running);
                    setMessage();
                }else{
                    Intent intent = new Intent(MainActivity.this, TrackerService.class);
                    intent.setAction(TrackerService.ACTION_STOP_FOREGROUND_SERVICE);
                    startService(intent);
                    startWorker();
                    mainBinding.appCompatButtonStart.setText(getString(R.string.button_text_start));
                    mainBinding.message.setText(getString(R.string.message_worker_stopped));
                    mainBinding.logs.setText(getString(R.string.log_for_stopped));
                    ref.child(getString(R.string.tracker_id));
                    LocationTrackerRequest request = new LocationTrackerRequest();
                    request.setIsActive(false);
                    ref.setValue(request);
                    forceStop();
                }
            }
        });

        mainBinding.showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        mainBinding.buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HistoryActivity.class));
            }
        });

//        bean();
    }

    void forceStop(){
        finish();
        throw new NullPointerException();
    }

    private void startWorker(){
        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                .addTag(TAG)
                .build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("Location", ExistingPeriodicWorkPolicy.REPLACE, periodicWork);

        Toast.makeText(MainActivity.this, "Location Worker Started : " + periodicWork.getId(), Toast.LENGTH_SHORT).show();

    }

    private void stopWorker(){
        WorkManager.getInstance().cancelAllWorkByTag(TAG);
        Toast.makeText(MainActivity.this, "Location Worker Stopped", Toast.LENGTH_SHORT).show();

    }

    private void setMessage(){
        ref.child(getString(R.string.tracker_id));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    double lat = 0;
                    double lng = 0;
                    HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                    HashMap<String, Object> location = (HashMap<String, Object>) value.get("location");
                    if(location != null){
                        lat = Double.parseDouble(location.get("latitude").toString());
                        lng = Double.parseDouble(location.get("longitude").toString());
                    }
                    String msg = "You are at: "+lat+", "+lng;
                    mainBinding.logs.setText(msg);
                    if(mainBinding.showMap.getVisibility() == View.INVISIBLE){
                        mainBinding.showMap.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("NewApi")
    private void bean(){

//        Date date = new Date();
//        date.setHours(23);
//        date.setMinutes(59);
//        date.setSeconds(59);
//        System.out.println("date "+date);
//
//
//        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
//
//
//        String firstDate = "2019-10-10 00:00:00";
//        String lastDate = "2019-10-10 23:59:59";
//
//        Date startDate = parseDate(firstDate);
//        Date endDate = parseDate(lastDate);
//
//        String sd = ymd.format(startDate)+"T"+hms.format(startDate);
//        String ed = ymd.format(endDate)+"T"+hms.format(endDate);
//
////        System.out.println("start date: "+sdf.format(startDate));
////        System.out.println("end date: "+sdf.format(endDate));
//
////        LocalDateTime ldt = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
////        System.out.println("local date time "+ldt.toString());
//
////        System.out.println("start local date"+toLocalDate(startDate).toString());
////        System.out.println("end local date"+toLocalDate(endDate).toString());
//        String includes = "checkpoint,location,employee";
//        moe.getLocationHistoryByEmployeeIdAndDateAndIsOnSite("43884",sd,ed,true,includes)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<LocationHistory>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @SuppressLint("NewApi")
//                    @Override
//                    public void onNext(List<LocationHistory> locationHistories) {
//                        Gson gson = new GsonBuilder().setExclusionStrategies(new JsonApiExclusionStrategy()).create();
//                        locationHistories.forEach(event -> {
//                            System.out.println("anone: "+gson.toJson(event));
//                        });
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

        /*moe.getLocationByTypeId("100", new String[] {"parent"})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Location>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("bebass");

                    }




                    @SuppressLint("NewApi")
                    @Override
                    public void onNext(List<Location> locations) {
                        System.out.println("moe");
                        Gson gson = new GsonBuilder().setExclusionStrategies(new JsonApiExclusionStrategy()).create();
                        locations.forEach(event -> {
                            System.out.println("anonee: "+gson.toJson(event.getParent()));
                        });

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(e);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(), "Succesfully det location", Toast.LENGTH_SHORT).show();
                        System.out.println("get loc");

                    }
                });*/
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @SuppressLint("NewApi")
    private static Date toDate(LocalDate localDate) {
        Date output = null;
        try {
            ZonedDateTime zdt = localDate.atStartOfDay(ZoneId.systemDefault());
            output = Date.from(zdt.toInstant());
        } catch (Exception e) {
        }
        return output;
    }



    @SuppressLint("NewApi")
    private static LocalDate toLocalDate(Date dateTime) {
        if (dateTime != null) {
           return Instant.ofEpochMilli(dateTime.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }
}
