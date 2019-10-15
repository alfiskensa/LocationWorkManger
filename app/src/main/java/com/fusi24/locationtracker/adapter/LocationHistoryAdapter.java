package com.fusi24.locationtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fusi24.locationtracker.maps.DetailHistoryActivityMap;

import com.fusi24.locationtracker.R;
import com.fusi24.locationtracker.model.jsonapi.LocationHistory;
import com.fusi24.locationtracker.util.JsonApiExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class LocationHistoryAdapter extends RecyclerView.Adapter<LocationHistoryAdapter.ViewHolder> {


    private List<LocationHistory> locationHistories;

    public LocationHistoryAdapter(List<LocationHistory> locationHistories){
        this.locationHistories = locationHistories;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LocationHistory locationHistory = locationHistories.get(position);
        Gson gson = new GsonBuilder().setExclusionStrategies(new JsonApiExclusionStrategy()).create();
        System.out.println("taki-kun: "+gson.toJson(locationHistory.getEmployee()));
        holder.name.setText(locationHistory.getEmployee().getName());
        if(locationHistory.getLocation().getLocationType().getId().equals("100")){
            holder.site.setText(locationHistory.getLocation().getName());
            holder.detailLocation.setText("Tidak ada detail lokasi");
        }else{
            holder.site.setText(locationHistory.getLocation().getParent().getParent().getName());
            holder.detailLocation.setText(locationHistory.getLocation().getName());
        }
        holder.date.setText(locationHistory.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return locationHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView site;
        TextView detailLocation;
        TextView date;

        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            site = itemView.findViewById(R.id.textViewSite);
            detailLocation = itemView.findViewById(R.id.textViewDetailLocation);
            date = itemView.findViewById(R.id.textViewDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailHistoryActivityMap.class);
                    intent.putExtra("id", locationHistories.get(position).getId());
                    intent.putExtra("lat",locationHistories.get(position).getLatitude());
                    intent.putExtra("lng",locationHistories.get(position).getLongitude());
                    intent.putExtra("empName",locationHistories.get(position).getEmployee().getName());
                    intent.putExtra("date", locationHistories.get(position).getDate().toString());
                    if(locationHistories.get(position).getLocation().getLocationType().getId().equals("100")){
                        intent.putExtra("site", locationHistories.get(position).getLocation().getName());
                        intent.putExtra("detail_loc","Tidak ada detail Lokasi");
                    }else{
                        intent.putExtra("site", locationHistories.get(position).getLocation().getParent().getParent().getName());
                        intent.putExtra("detail_loc",locationHistories.get(position).getLocation().getName());
                    }

//                    intent.putExtra("list", (Serializable) locationHistories);
                    context.startActivity(intent);
                }
            });
        }


    }
}
