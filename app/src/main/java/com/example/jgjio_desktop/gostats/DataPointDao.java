package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.arch.paging.DataSource;
import android.provider.ContactsContract;

import java.util.List;

@Dao
public interface DataPointDao {
    @Insert
    void insert(DataPoint dataPoint);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(DataPoint dataPoint);

    @Delete
    void delete(DataPoint dataPoint);

    @Query("SELECT * FROM data_point WHERE list_id = :listId")
    LiveData<List<DataPoint>> getList(Double listId);

    @Query("SELECT * FROM data_point")
    LiveData<List<DataPoint>> loadAllDataPoints();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDataPoints(List<DataPoint> dataPoints);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDataPoint(DataPoint dataPoint);

    @Query("SELECT * FROM data_point WHERE list_id = :listId ORDER By id")
    public abstract DataSource.Factory<Integer, DataPoint> getListById(long listId);

    @Query("SELECT MAX(value) FROM data_point WHERE list_id = :listID")
    double getMaxValue(Double listID);

    @Query("SELECT MIN(value) FROM data_point WHERE list_id = :listID")
    double getMinValue(Double listID);

    @Query("SELECT COUNT(*) FROM data_point WHERE list_id = :listID")
    long getNumberOfDataPointsInList(Double listID);

}
