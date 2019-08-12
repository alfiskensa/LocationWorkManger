package com.pratikbutani.workerexample;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.pratikbutani.workerexample.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    private static final String TAG = "LocationUpdate";

    ActivityMainBinding mainBinding;

    String periodicId;

    TextToSpeech tTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

        setSupportActionBar(mainBinding.toolbar);

        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }

        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                .addTag(TAG)
                .build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("Location", ExistingPeriodicWorkPolicy.REPLACE, periodicWork);

        Toast.makeText(MainActivity.this, "Location Worker Started : " + periodicWork.getId(), Toast.LENGTH_SHORT).show();

//        mainBinding.appCompatButtonStart.setText(getString(R.string.button_text_stop));
        if(periodicWork.getId() != null){
            periodicId = periodicWork.getId().toString();
        }
        mainBinding.message.setText("Process ID : " + periodicId);
        mainBinding.logs.setText(getString(R.string.log_for_running));

        try {
            if (isWorkScheduled(WorkManager.getInstance().getWorkInfosByTag(TAG).get())) {
//				mainBinding.appCompatButtonStart.setText(getString(R.string.button_text_stop));
                mainBinding.appCompatButtonStart.setBackgroundResource(R.drawable.power_on);
                mainBinding.message.setText(getString(R.string.message_worker_running));
                mainBinding.logs.setText(getString(R.string.log_for_running));
            } else {
//				mainBinding.appCompatButtonStart.setText(getString(R.string.button_text_start));
                mainBinding.appCompatButtonStart.setBackgroundResource(R.drawable.power_off);
                mainBinding.message.setText(getString(R.string.message_worker_stopped));
                mainBinding.logs.setText(getString(R.string.log_for_stopped));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        mainBinding.appCompatButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mainBinding.appCompatButtonStart.getText().toString().equalsIgnoreCase(getString(R.string.button_text_start))) {
                    // START Worker
                    PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                            .addTag(TAG)
                            .build();
                    WorkManager.getInstance().enqueueUniquePeriodicWork("Location", ExistingPeriodicWorkPolicy.REPLACE, periodicWork);

                    Toast.makeText(MainActivity.this, "Location Worker Started : " + periodicWork.getId(), Toast.LENGTH_SHORT).show();

                    v.startAnimation(animScale);
                    mainBinding.appCompatButtonStart.setBackgroundResource(R.drawable.power_on);

                    mainBinding.appCompatButtonStart.setText(getString(R.string.button_text_stop));
                    if(periodicWork.getId() != null){
                        periodicId = periodicWork.getId().toString();
                    }
                    mainBinding.message.setText("Process ID : " + periodicId);
                    mainBinding.logs.setText(getString(R.string.log_for_running));
                } else {

                    WorkManager.getInstance().cancelAllWorkByTag(TAG);

                    mainBinding.appCompatButtonStart.setText(getString(R.string.button_text_start));
                    mainBinding.appCompatButtonStart.setBackgroundResource(R.drawable.power_off);
                    mainBinding.message.setText(getString(R.string.message_worker_stopped));
                    mainBinding.logs.setText(getString(R.string.log_for_stopped));
                    v.startAnimation(animScale);
                }
            }
        });

        tTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                //method untuk mendeteksi suara dari text

                if(status != TextToSpeech.ERROR) {
                    tTS.setLanguage(Locale.getDefault());
//                    tTS.setPitch(1f);
                    tTS.setSpeechRate(0.7f);
                    //menggunakan bahasa US (amerika)

                }
            }
        });

    }

    private boolean isWorkScheduled(List<WorkInfo> workInfos) {
        boolean running = false;
        if (workInfos == null || workInfos.size() == 0) return false;
        for (WorkInfo workStatus : workInfos) {
            running = workStatus.getState() == WorkInfo.State.RUNNING | workStatus.getState() == WorkInfo.State.ENQUEUED;
        }
        return running;
    }

    /**
     * All about permission
     */
    private boolean checkLocationPermission() {
        int result3 = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);
        int result4 = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        return result3 == PackageManager.PERMISSION_GRANTED &&
                result4 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean coarseLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean fineLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (coarseLocation && fineLocation)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
