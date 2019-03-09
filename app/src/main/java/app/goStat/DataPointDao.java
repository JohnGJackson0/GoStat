package app.goStat;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.arch.paging.DataSource;

import java.util.List;

@Dao
public interface DataPointDao {
    @Insert
    void insert(DataPoint dataPoint);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(DataPoint dataPoint);

    @Query("DELETE FROM data_point WHERE list_id = :listId AND isEnabled=0")
    void deleteDisabledDataPoints(int listId);

    @Query("SELECT * FROM data_point WHERE list_id = :listId")
    LiveData<List<DataPoint>> getList(int listId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDataPoints(List<DataPoint> dataPoints);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDataPoint(DataPoint dataPoint);

    @Query("SELECT * FROM data_point WHERE list_id = :listId ORDER By id")
    DataSource.Factory<Integer, DataPoint> getListById(long listId);

    @Query("SELECT COUNT(*) FROM data_point WHERE list_id = :listID")
    LiveData<Long> getNumberOfDataPointsInList(int listID);

    @Query("SELECT COUNT(*) FROM data_point WHERE list_id = :listID")
    Long getStaticNumberOfDataPointsInList(int listID);

    @Query("SELECT * FROM data_point WHERE list_id = :listId AND isEnabled = 1 LIMIT 26")
    LiveData<List<DataPoint>> getEditableListPreview(int listId);

}
