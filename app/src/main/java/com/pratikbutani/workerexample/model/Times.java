package com.pratikbutani.workerexample.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Data;

@Data
@Entity(tableName = "tbl_times")
public class Times {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Date date;
}
