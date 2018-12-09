package com.example.jgjio_desktop.gostats;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FrequencyIntervalDao {
    @Insert
    void insert(FrequencyInterval freqInterval);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(FrequencyInterval freqInterval);

    @Delete
    void delete(FrequencyInterval frequencyInterval);

    @Query("SELECT * FROM frequency_interval WHERE list_id = :listId")
    LiveData<List<FrequencyInterval>> getList(int listId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFrequencyIntervals(List<FrequencyInterval> frequencyIntervals);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFrequencyInterval(FrequencyInterval frequencyInterval);

    @Query("SELECT * FROM frequency_interval WHERE list_id = :listId ORDER By id")
    DataSource.Factory<Integer, FrequencyInterval> getListById(long listId);

    @Query("SELECT COUNT(*) FROM frequency_interval WHERE list_id = :listID")
    LiveData<Long> getNumberOfIntervalsInList(int listID);

    @Query("SELECT MAX(frequency) FROM frequency_interval WHERE list_id = :listID")
    LiveData<Integer> getMaxValue(int listID);

    @Query("SELECT * FROM frequency_interval WHERE list_id = :listId LIMIT 10")
    LiveData<List<FrequencyInterval>> getFrequencyTablePreview(int listId);
}
