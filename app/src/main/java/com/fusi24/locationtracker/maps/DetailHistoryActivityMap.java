package com.fusi24.locationtracker.maps;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.fusi24.locationtracker.R;
import com.fusi24.locationtracker.model.jsonapi.LocationHistory;
import com.fusi24.locationtracker.util.JsonApiExclusionStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;

public class DetailHistoryActivityMap extends BaseMapActivity {

    private HashMap<String, Marker> mMarkers = new HashMap<>();


    @Override
    protected void startDemo() {
        setInfo();
        setMarker();
    }

    private void setInfo(){
        getMap().setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);
                return info;
            }
        });
    }

    @SuppressLint("NewApi")
    private void setMarker(){
        //mMarkers = new HashMap<>();

        String id = getIntent().getStringExtra("id");
        Double lat = getIntent().getDoubleExtra("lat",0.0);
        Double lng = getIntent().getDoubleExtra("lng",0.0);
        String empName = getIntent().getStringExtra("empName");
        String date = getIntent().getStringExtra("date");
        String site = getIntent().getStringExtra("site");
        String detail_loc = getIntent().getStringExtra("detail_loc");
        String snippet = "Site: "+site;
        snippet += "\nDetail Lokasi: "+detail_loc;
        snippet += "\n"+date;
//        List<LocationHistory> list = (List<LocationHistory>) getIntent().getSerializableExtra("list");
//        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
//        list.forEach(event -> {
//            LatLng point = new LatLng(event.getLatitude(), event.getLongitude());
//            mMarkers.put(event.getId(), mMap
//                    .addMarker(new MarkerOptions()
//                            .title(event.getEmployee().getName())
//                            .snippet(event.getDate().toString())
//                            .position(new LatLng(event.getLatitude(), event.getLongitude()))));
//            options.add(point);
//        });

        LatLng location = new LatLng(lat,lng);

        System.out.println(location.toString());

        if(!mMarkers.containsKey(id)){
            mMarkers.put(id, getMap().addMarker(new MarkerOptions().title(empName).snippet(snippet).position(location)));
        }else{
            mMarkers.get(id).setPosition(location);
        }

//        mMap.addPolyline(options);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            builder.include(marker.getPosition());
        }

        final LatLngBounds bounds = builder.build();
//        getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
//        getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
    }
}
