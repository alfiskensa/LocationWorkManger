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

import com.fusi24.locationtracker.databinding.ActivityMainBinding;
import com.fusi24.locationtracker.service.TrackerService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;

    private ActivityMainBinding mainBinding;
    private DatabaseReference ref;
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

        mainBinding.appCompatButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainBinding.appCompatButtonStart.getText().toString().equalsIgnoreCase(getString(R.string.button_text_start))){
                    Intent intent = new Intent(MainActivity.this, TrackerService.class);
                    intent.setAction(TrackerService.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent);
                    mainBinding.appCompatButtonStart.setText(getString(R.string.button_text_stop));
                    mainBinding.message.setText(R.string.message_worker_running);
                    setMessage();
                }else{
                    Intent intent = new Intent(MainActivity.this, TrackerService.class);
                    intent.setAction(TrackerService.ACTION_STOP_FOREGROUND_SERVICE);
                    startService(intent);
                    mainBinding.appCompatButtonStart.setText(getString(R.string.button_text_start));
                    mainBinding.message.setText(getString(R.string.message_worker_stopped));
                    mainBinding.logs.setText(getString(R.string.log_for_stopped));
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

    private void setMessage(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                    double lat = Double.parseDouble(value.get("latitude").toString());
                    double lng = Double.parseDouble(value.get("longitude").toString());
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
