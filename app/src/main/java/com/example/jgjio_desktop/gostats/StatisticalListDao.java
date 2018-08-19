package com.example.jgjio_desktop.gostats;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StatisticalListDao {

    @Query("SELECT * FROM list ORDER BY id")
    List<StatisticalList> loadAllLists();

    @Insert
    void insert(StatisticalList statisticalList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(StatisticalList statisticalList);

    @Delete
    void delete(StatisticalList statisticalList);
}
