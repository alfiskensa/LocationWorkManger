package com.fusi24.locationtracker.maps;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Person;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.fusi24.locationtracker.R;
import com.fusi24.locationtracker.apiservice.ApiUtils;
import com.fusi24.locationtracker.apiservice.BaseBCUI2Service;
import com.fusi24.locationtracker.config.Constants;
import com.fusi24.locationtracker.model.EmployeeClusterItem;
import com.fusi24.locationtracker.model.jsonapi.Employee;
import com.fusi24.locationtracker.util.JsonApiExclusionStrategy;
import com.fusi24.locationtracker.util.MultiDrawable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.moshi.Json;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MapsActivity extends BaseMapActivity implements ClusterManager.OnClusterClickListener<EmployeeClusterItem>, ClusterManager.OnClusterInfoWindowClickListener<EmployeeClusterItem>, ClusterManager.OnClusterItemClickListener<EmployeeClusterItem>, ClusterManager.OnClusterItemInfoWindowClickListener<EmployeeClusterItem> {

    private ClusterManager<EmployeeClusterItem> mClusterManager;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private HashMap<String, EmployeeClusterItem> mCluster = new HashMap<>();

    private BaseBCUI2Service moe;

    private class EmployeeRenderer extends DefaultClusterRenderer<EmployeeClusterItem>{

        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public EmployeeRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);
            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding,padding,padding,padding);
            mIconGenerator.setContentView(mImageView);

        }

        private Drawable getDrawable(String path){
            System.out.println(path);
            String url = Constants.URL_PHOTO+path.substring(14);
            System.out.println("get drawable: "+url);
            Picasso.get().load(url)
                    .resize(150,150)
                    .centerCrop()
                    .into(target);
            return drawable;
        }

        @Override
        protected void onBeforeClusterItemRendered(EmployeeClusterItem item, MarkerOptions markerOptions) {
            String url = Constants.URL_PHOTO+item.getEmployee().get("urlPhoto").toString().substring(14);
            System.out.println(url);
            Picasso.get().load(url)
                    .resize(150,150)
                    .centerCrop()
                    .into(mImageView);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getEmployee().get("name").toString());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<EmployeeClusterItem> cluster, MarkerOptions markerOptions) {
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for(EmployeeClusterItem emp : cluster.getItems()){
                if(profilePhotos.size() == 4) break;
                System.out.println(emp.getEmployee().get("urlPhoto"));
                Drawable drawable = getDrawable(emp.getEmployee().get("urlPhoto").toString());
                drawable.setBounds(0,0,width,height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0,0,width,height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<EmployeeClusterItem> cluster) {
            return cluster.getSize() > 1;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<EmployeeClusterItem> cluster) {
        String firstName = cluster.getItems().iterator().next().getEmployee().get("name").toString();
        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<EmployeeClusterItem> cluster) {

    }

    @Override
    public boolean onClusterItemClick(EmployeeClusterItem employee) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(EmployeeClusterItem employee) {

    }


    @Override
    protected void startDemo() {

        moe = ApiUtils.getBCUI2Service();

        mClusterManager = new ClusterManager<EmployeeClusterItem>(this,getMap());
        mClusterManager.setRenderer(new EmployeeRenderer());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        subscribeToUpdates();
        initCamera();
    }

    @SuppressLint("NewApi")
    void initCamera(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Map<String, Object>>  value = (HashMap) dataSnapshot.getValue();
                value = value.entrySet().stream().filter(e -> e.getValue().get("isActive").equals(true)).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                if(value.entrySet().size() > 1){
                    value.entrySet().forEach(event -> {
                        Map<String, Object> loc = (Map<String, Object>) event.getValue().get("location");
                        double lat = Double.parseDouble(loc.get("latitude").toString());
                        double lng = Double.parseDouble(loc.get("longitude").toString());
                        LatLng location = new LatLng(lat, lng);
                        builder.include(location);
                    });
                    final LatLngBounds bounds = builder.build();
                    getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
                }else{
                    value.entrySet().forEach(event -> {
                        Map<String, Object> loc = (Map<String, Object>) event.getValue().get("location");
                        double lat = Double.parseDouble(loc.get("latitude").toString());
                        double lng = Double.parseDouble(loc.get("longitude").toString());
                        LatLng location = new LatLng(lat, lng);
                        getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void subscribeToUpdates() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        ref.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                setMarker(dataSnapshot);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                setMarker(dataSnapshot);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Log.d(TrackerService.TAG, "Failed to read value.", error.toException());
//            }
//        });
    }
    @SuppressLint("NewApi")
    private void setMarker(DataSnapshot dataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        String key = dataSnapshot.getKey();
        System.out.println(key);
        Map<String, Map<String, Object>>  value = (HashMap) dataSnapshot.getValue();
        value = value.entrySet().stream().filter(e -> e.getValue().get("isActive").equals(true)).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        mClusterManager.clearItems();
        value.entrySet().forEach(event -> {
            Map<String, Object> loc = (Map<String, Object>) event.getValue().get("location");
            Map<String, Object> emp = (Map<String, Object>) event.getValue().get("employee");
            System.out.println("emp: "+emp);
            double lat = Double.parseDouble(loc.get("latitude").toString());
            double lng = Double.parseDouble(loc.get("longitude").toString());
            LatLng location = new LatLng(lat, lng);
            EmployeeClusterItem item = new EmployeeClusterItem(location,emp);
            mClusterManager.addItem(item);
//            if(!emp.getId().equals(event.getKey())){
//                list.add(item);
//            }else{
//                if(!list.stream().filter(e -> e.getEmployee().getId().equals(event.getKey())).findFirst().isPresent()){
//                    list.add(item);
//                }else {
//                    list.removeIf(e -> e.getEmployee().getId().equals(item.getEmployee().getId()));
//                    list.add(item);
//                }
//            }

        });
//        mClusterManager.clearItems();
//        mClusterManager.addItems(list);
        mClusterManager.cluster();
//            if (!mMarkers.containsKey(event.getKey())) {
//                mMarkers.put(event.getKey(), getMap().addMarker(new MarkerOptions().title(event.getKey()).position(location)));
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                for (Marker marker : mMarkers.values()) {
//                    builder.include(marker.getPosition());
//                }
////                getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20));
//                mClusterManager.cluster();
//            } else {
//                mMarkers.get(event.getKey()).setPosition(location);
//            }


    }




    Drawable drawable;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            drawable = new BitmapDrawable(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


}
