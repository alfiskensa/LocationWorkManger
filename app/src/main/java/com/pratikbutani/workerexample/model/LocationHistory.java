package com.pratikbutani.workerexample.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Data
@Entity(tableName = "locationTbl")
public class LocationHistory {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Double longitude;

    private Double latitude;

    private Date date;

    private String deviceInfo;

    private String employeeId;

    public Integer checkpointId;

}
