package com.fusi24.locationtracker.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.fusi24.locationtracker.model.LocationHistory;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocationHistory locationHistory);

    @Query("DELETE FROM locationTbl")
    void delete();

    @Query("SELECT * FROM locationTbl where date in (SELECT max(date) FROM locationTbl)")
    LocationHistory selectLast();

    @Query("SELECT * FROM locationTbl")
    List<LocationHistory> selectAll();
}
