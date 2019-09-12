package com.fusi24.locationtracker.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.fusi24.locationtracker.model.LocationHistory;
import com.fusi24.locationtracker.model.LocationTrackerRequest;
import com.fusi24.locationtracker.model.Times;
import com.fusi24.locationtracker.util.DateTypeConverter;

@Database(entities = {LocationHistory.class, Times.class}, version = 3, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mInstance;
    private static final String DATABASE_NAME = "location-database";
    public abstract LocationDao locationDao();
    public abstract TimesDao timesDao();
   // public abstract LocationTrackerDao trackerRequestDao();

    private static  volatile AppDatabase INSTANCE;

    public synchronized static AppDatabase getInstance(Context context){
        if(mInstance == null){
            mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }
}
