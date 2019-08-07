package com.pratikbutani.workerexample.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pratikbutani.workerexample.model.Times;

@Dao
public interface TimesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Times time);

    @Query("DELETE FROM tbl_times")
    void delete();

    @Query("SELECT * FROM tbl_times LIMIT 1")
    Times selectLast();
}
