//package com.fusi24.locationtracker.data;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import com.fusi24.locationtracker.model.LocationTrackerRequest;
//
//@Dao
//public interface LocationTrackerDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(LocationTrackerRequest locationTrackerRequest);
//
//    @Query("DELETE from locationTrackerRequestTbl")
//    void delete();
//
//    @Update
//    void update(LocationTrackerRequest locationTrackerRequest);
//
//    @Query("SELECT * FROM locationTrackerRequestTbl LIMIT 1")
//    LocationTrackerRequest selectLast();
//}
