package com.fusi24.locationtracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.fusi24.locationtracker.databinding.ActivityMainBinding;
import com.fusi24.locationtracker.service.TrackerService;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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
                    mainBinding.logs.setText(getString(R.string.log_for_running));
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
    }
}
