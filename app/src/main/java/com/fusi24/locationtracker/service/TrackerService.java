package com.fusi24.locationtracker.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.fusi24.locationtracker.R;
import com.fusi24.locationtracker.apiservice.ApiUtils;
import com.fusi24.locationtracker.apiservice.BaseApiService;
import com.fusi24.locationtracker.apiservice.BaseBCUI2Service;
import com.fusi24.locationtracker.data.AppDatabase;
import com.fusi24.locationtracker.data.ManualTime;
import com.fusi24.locationtracker.model.LocationHistory;
import com.fusi24.locationtracker.model.LocationHistoryResponse;
import com.fusi24.locationtracker.model.LocationTrackerRequest;
import com.fusi24.locationtracker.model.Times;
import com.fusi24.locationtracker.model.jsonapi.Employee;
import com.fusi24.locationtracker.util.JsonApiExclusionStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moe.banana.jsonapi2.Document;
import moe.banana.jsonapi2.ObjectDocument;

public class TrackerService extends Service {

    public static final String TAG = TrackerService.class.getSimpleName();

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public static final String ACTION_PAUSE = "ACTION_PAUSE";

    public static final String ACTION_PLAY = "ACTION_PLAY";

    private BaseApiService mApiService;

    private AppDatabase db;

    private BaseBCUI2Service moe;

    private Employee emp;

    TextToSpeech tTS;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        this.stopSelf();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = AppDatabase.getInstance(getApplicationContext());
        mApiService = ApiUtils.getAPIService();
        moe = ApiUtils.getBCUI2Service();
        getEmployee(getString(R.string.tracker_id));
        tTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                //method untuk mendeteksi suara dari text

                if(status != TextToSpeech.ERROR) {
                    tTS.setLanguage(Locale.getDefault());
                    tTS.setSpeechRate(0.8f);
                    tTS.setPitch(1f);
                    //menggunakan bahasa US (amerika)

                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            switch (action){
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundServce();
                    Toast.makeText(getApplicationContext(), "Foreground Service Started", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    Toast.makeText(getApplicationContext(), "Foreground Service is Stopped", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_PLAY:
                    Toast.makeText(getApplicationContext(), "You click Play button.", Toast.LENGTH_LONG).show();
                    break;
                case ACTION_PAUSE:
                    Toast.makeText(getApplicationContext(), "You click Pause button.", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void startForegroundServce(){
        buildNotification();
        requestLocationUpdates();
    }

    public void stopForegroundService(){
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        client.removeLocationUpdates(pendingIntent);
        Log.d(TAG, "Stop foreground service.");

        // Stop foreground service and remove the notification.
        //stopForeground(true);

        // Stop the foreground service.
        stopSelf();

    }

    private void getEmployee(String id){
        moe.findById(id)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Employee>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Employee employee) {
                        emp = employee;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void buildNotification() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            String channelId = createNotificationChannel("my_service", "My Foreground Service");
//            String stop = "stop";
//            registerReceiver(stopReceiver, new IntentFilter(stop));
//            PendingIntent broadcastIntent = PendingIntent.getBroadcast(
//                    this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
//            // Create the persistent notification
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText(getString(R.string.notification_text))
//                    .setOngoing(true)
//                    .setContentIntent(broadcastIntent)
//                    .setSmallIcon(R.drawable.ic_tracker);
//            startForeground(new Random().nextInt(50)+1, builder.build());
//        }else{
            // Create notification default intent.
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            // Create notification builder.
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            // Make notification show big text.
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle("Location Tracker implemented by foreground service.");
            bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.");
            // Set big text style.
            builder.setStyle(bigTextStyle);

            builder.setWhen(System.currentTimeMillis());
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSmallIcon(R.drawable.ic_tracker);
            // Make the notification max priority.
            builder.setPriority(Notification.PRIORITY_MAX);
            // Make head-up notification.
            builder.setFullScreenIntent(pendingIntent, true);

//            // Add Play button intent in notification.
//            Intent playIntent = new Intent(this, TrackerService.class);
//            playIntent.setAction(ACTION_PLAY);
//            PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
//            NotificationCompat.Action playAction = new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pendingPlayIntent);
//            builder.addAction(playAction);
//
//            // Add Pause button intent in notification.
//            Intent pauseIntent = new Intent(this, TrackerService.class);
//            pauseIntent.setAction(ACTION_PAUSE);
//            PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
//            NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent);
//            builder.addAction(prevAction);

            // Build the notification.
            Notification notification = builder.build();

            startForeground(new Random().nextInt(50)+1, notification);
//        }

    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(channel);
        return channelId;
    }


    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = getString(R.string.firebase_path) + "/" + getString(R.string.tracker_id);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d(TAG, "location update " + location);
                        LocationTrackerRequest request1 = new LocationTrackerRequest();
                        request1.setLocation(location);
                        request1.setIsActive(true);
                        Gson gson = new GsonBuilder().setExclusionStrategies(new JsonApiExclusionStrategy()).create();
                        if(emp != null){
                            request1.setEmployee(gson.fromJson(gson.toJson(emp), Object.class));
                            System.out.println(gson.toJsonTree(emp).toString());
                            ref.setValue(request1);
                            sendLocation(location);
                        }
                    }
                }
            }, null);
        }
    }

    private void sendLocation(Location location){
        LocationHistory locationHistory = new LocationHistory();
        locationHistory.setLatitude(location.getLatitude());
        locationHistory.setLongitude(location.getLongitude());
        locationHistory.setEmployeeId(43884);
        LocationHistory checkpoint = db.locationDao().selectLast();
        if(checkpoint != null){
            locationHistory.setCheckpointId(checkpoint.getId());
        }
        String deviceInfo="Device Info:";
        deviceInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        deviceInfo += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        deviceInfo += "\n Device: " + android.os.Build.DEVICE;
        deviceInfo += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
        locationHistory.setDeviceInfo(deviceInfo);
        mApiService.locationSend(locationHistory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LocationHistoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LocationHistoryResponse locationHistoryResponse) {

                        Gson gson = new Gson();
                        List<LocationHistory> list = db.locationDao().selectAll();
                        if(list.size() == 0){
                            db.locationDao().insert(locationHistoryResponse.getLocationHistory());
                            insertTime(locationHistoryResponse.getLocationHistory().getDate());
                            System.out.println("insert: "+gson.toJson(db.locationDao().selectLast()));
                        }else{
                            if(locationHistoryResponse.getLocationHistory().getId() != null){
                                db.locationDao().delete();
                                db.locationDao().insert(locationHistoryResponse.getLocationHistory());
                                System.out.println("update insert: "+gson.toJson(db.locationDao().selectLast()));
                            }
                        }
                        Times time = db.timesDao().selectLast();
                        System.out.println("time in db: "+time.toString());
                        System.out.println("time from res: "+locationHistoryResponse.getLocationHistory().getDate());
                        System.out.println("total jam: "+calculateTime(locationHistoryResponse.getLocationHistory().getDate()));
                        ManualTime manualTime = calculateTime(locationHistoryResponse.getLocationHistory().getDate());
                        if(manualTime.getDays() > 0){
                            db.locationDao().delete();
                            db.timesDao().delete();
                            System.out.println("data terhapus");
                        }
                        sendNotification(locationHistoryResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(), "Succesfully send location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void sendNotification(LocationHistoryResponse locationHistoryResponse){

        if(locationHistoryResponse.getMessage() != null){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.app_name))
                    .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                    .setContentTitle(locationHistoryResponse.getMessage())
                    .setContentText("You are at " + getCompleteAddressString(locationHistoryResponse.getLocationHistory().getLatitude(),
                            locationHistoryResponse.getLocationHistory().getLongitude()))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("You are at " + getCompleteAddressString(
                            locationHistoryResponse.getLocationHistory().getLatitude(),
                            locationHistoryResponse.getLocationHistory().getLongitude())));
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

            Random random = new Random();
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(random.nextInt(50)+1, builder.build());
            startSpeech(locationHistoryResponse.getMessage());

        }

    }

    private void startSpeech(String message) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tTS.speak(message, TextToSpeech.QUEUE_FLUSH,null,null);


        } else {
            tTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    private void insertTime(Date date){
        Times time = new Times();
        time.setDate(date);
        db.timesDao().insert(time);
    }

    private ManualTime calculateTime(Date currentDate){
        Date d1 = currentDate;
        Date d2 = db.timesDao().selectLast().getDate();

        //in milliseconds
        long diff = d1.getTime() - d2.getTime();
        ManualTime manualTime = new ManualTime();

        manualTime.setSeconds(diff / 1000 % 60);
        manualTime.setMinutes(diff / (60 * 1000) % 60);
        manualTime.setHours(diff / (60 * 60 * 1000) % 24);
        manualTime.setDays(diff / (24 * 60 * 60 * 1000));

        return manualTime;

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

}
