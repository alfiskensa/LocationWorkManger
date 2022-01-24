package com.pratikbutani.workerexample;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.pratikbutani.workerexample.apiservice.ApiUtils;
import com.pratikbutani.workerexample.apiservice.BaseApiService;
import com.pratikbutani.workerexample.data.AppDatabase;
import com.pratikbutani.workerexample.data.ManualTime;
import com.pratikbutani.workerexample.model.LocationHistory;
import com.pratikbutani.workerexample.model.LocationHistoryResponse;
import com.pratikbutani.workerexample.model.Times;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyWorker extends Worker {

	private static final String DEFAULT_START_TIME = "08:00";
	private static final String DEFAULT_END_TIME = "23:59";

	private static final String TAG = "MyWorker";

	/**
	 * The desired interval for location updates. Inexact. Updates may be more or less frequent.
	 */
	private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

	/**
	 * The fastest rate for active location updates. Updates will never be more frequent
	 * than this value.
	 */
	private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
			UPDATE_INTERVAL_IN_MILLISECONDS / 2;
	/**
	 * The current location.
	 */
	private Location mLocation;

	/**
	 * Provides access to the Fused Location Provider API.
	 */
	private FusedLocationProviderClient mFusedLocationClient;

	private Context mContext;
	/**
	 * Callback for changes in location.
	 */
	private LocationCallback mLocationCallback;

	private BaseApiService mApiService;

	private LocationHistory locationHistory;

	private LocationHistory currentLocation;

	private String message;

	private AppDatabase db;

	public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
		mContext = context;
		db = AppDatabase.getInstance(mContext);

	}

	@NonNull
	@Override
	public Result doWork() {
		Log.d(TAG, "doWork: Done");

		Log.d(TAG, "onStartJob: STARTING JOB..");

		DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		String formattedDate = dateFormat.format(date);

		mApiService = ApiUtils.getAPIService();

		locationHistory = new LocationHistory();

		try {
			Date currentDate = dateFormat.parse(formattedDate);
			Date startDate = dateFormat.parse(DEFAULT_START_TIME);
			Date endDate = dateFormat.parse(DEFAULT_END_TIME);


				mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
				mLocationCallback = new LocationCallback() {
					@Override
					public void onLocationResult(LocationResult locationResult) {
						super.onLocationResult(locationResult);
					}
				};

				LocationRequest mLocationRequest = new LocationRequest();
				mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
				mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
				mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

				try {
					mFusedLocationClient
							.getLastLocation()
							.addOnCompleteListener(new OnCompleteListener<Location>() {
								@Override
								public void onComplete(@NonNull Task<Location> task) {
									if (task.isSuccessful() && task.getResult() != null) {
										mLocation = task.getResult();
										Log.d(TAG, "Location : " + mLocation);
										System.out.println(mLocation.getLatitude());
										System.out.println(mLocation.getLongitude());

										// Create the NotificationChannel, but only on API 26+ because
										// the NotificationChannel class is new and not in the support library
										if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
											CharSequence name = mContext.getString(R.string.app_name);
											String description = mContext.getString(R.string.app_name);
											int importance = NotificationManager.IMPORTANCE_DEFAULT;
											NotificationChannel channel = new NotificationChannel(mContext.getString(R.string.app_name), name, importance);
											channel.setDescription(description);
											// Register the channel with the system; you can't change the importance
											// or other notification behaviors after this
											NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
											notificationManager.createNotificationChannel(channel);
										}

										locationHistory.setLatitude(mLocation.getLatitude());
										locationHistory.setLongitude(mLocation.getLongitude());
										locationHistory.setEmployeeId(43884);
										String deviceInfo="Device Info:";
										deviceInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
										deviceInfo += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
										deviceInfo += "\n Device: " + android.os.Build.DEVICE;
										deviceInfo += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
										locationHistory.setDeviceInfo(deviceInfo);
										currentLocation = db.locationDao().selectLast();
										Gson gson = new Gson();
										if(currentLocation != null){
											locationHistory.setCheckpointId(currentLocation.getId());
										}
										System.out.println("current checkpoint : "+gson.toJson(currentLocation));
										System.out.println("will be send  : "+gson.toJson(locationHistory));
										sendLocation(locationHistory);

										mFusedLocationClient.removeLocationUpdates(mLocationCallback);
									} else {
										Log.w(TAG, "Failed to get location.");
									}
								}
							});
				} catch (SecurityException unlikely) {
					Log.e(TAG, "Lost location permission." + unlikely);
				}

				try {
					mFusedLocationClient.requestLocationUpdates(mLocationRequest, null);
				} catch (SecurityException unlikely) {
					//Utils.setRequestingLocationUpdates(this, false);
					Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
				}

		} catch (ParseException ignored) {

		}
		Log.d("test","test");
		return Result.success();
	}

	private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
		String strAdd = "";
		Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
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

	private void sendLocation(LocationHistory locationHistory){
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
						Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete() {
						Toast.makeText(mContext, "Succesfully send location", Toast.LENGTH_SHORT).show();
					}
				});
	}

	void sendNotification(LocationHistoryResponse locationHistoryResponse){
		if(locationHistoryResponse.getMessage() != null){
			NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.app_name))
					.setSmallIcon(android.R.drawable.ic_menu_mylocation)
					.setContentTitle(locationHistoryResponse.getMessage())
					.setContentText("You are at " + getCompleteAddressString(locationHistoryResponse.getLocationHistory().getLatitude(),
							locationHistoryResponse.getLocationHistory().getLongitude()))
					.setPriority(NotificationCompat.PRIORITY_DEFAULT)
					.setStyle(new NotificationCompat.BigTextStyle().bigText("You are at " + getCompleteAddressString(
							locationHistoryResponse.getLocationHistory().getLatitude(),
							locationHistoryResponse.getLocationHistory().getLongitude())));
			NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

			Random random = new Random();
			// notificationId is a unique int for each notification that you must define
			notificationManager.notify(random.nextInt(50)+1, builder.build());
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

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}