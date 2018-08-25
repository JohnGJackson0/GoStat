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
public interface StatisticalListDao {

    @Query("SELECT * FROM list ORDER BY id")
    LiveData<List<StatisticalList>> loadAllLists();

    @Insert
    void insert(StatisticalList statisticalList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(StatisticalList statisticalList);

    @Delete
    void delete(StatisticalList statisticalList);

    @Query("SELECT count(*) FROM list")
    int getListCount();

    @Query("SELECT name FROM list WHERE id = :id LIMIT 1;")
    String getListName(int id);

    @Query("DELETE FROM list")
    void nukeTable();

    //We set room to auto generate keys so the app will
    //use this to get entries we just added
    //it's important that this gets called before adding the query
    //then you simply add 1, because the program uses async tasks
    //and it might not be finished by the time this is called.
    @Query("SELECT * FROM list ORDER BY id DESC LIMIT 1;")
    int getIdOfLastEntry();

}
