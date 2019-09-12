package com.fusi24.locationtracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.fusi24.locationtracker.data.AppDatabase;
import com.fusi24.locationtracker.databinding.ActivityMainBinding;
import com.fusi24.locationtracker.model.LocationTrackerRequest;
import com.fusi24.locationtracker.service.MyWorker;
import com.fusi24.locationtracker.service.TrackerService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;

    private static final String TAG = "BackgroundLocationUpdate";

    private ActivityMainBinding mainBinding;
    private DatabaseReference ref;
    private AppDatabase db;
    private String msg;

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
                    if(value != null){
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
}
