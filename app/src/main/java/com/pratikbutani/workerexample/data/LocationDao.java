package com.pratikbutani.workerexample.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pratikbutani.workerexample.model.LocationHistory;

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
