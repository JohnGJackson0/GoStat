package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
    LiveData<List<DataPoint>> getList(int listId);

    @Query("SELECT * FROM data_point")
    LiveData<List<DataPoint>> loadAllDataPoints();

}
