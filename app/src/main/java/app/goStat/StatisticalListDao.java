package app.goStat;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StatisticalListDao {
    @Query("SELECT * FROM list ORDER BY id")
    LiveData<List<StatisticalList>> loadAllLists();

    @Insert
    long insert(StatisticalList statisticalList);

    @Query("DELETE FROM list WHERE id = :id")
    void deleteListById(int id);

    @Query("SELECT count(*) FROM list")
    LiveData<Long> getListCount();

    @Query("SELECT * FROM list ORDER By id")
    android.arch.paging.DataSource.Factory<Integer, StatisticalList> getAllLists();

    @Query("SELECT name FROM list WHERE id = :id LIMIT 1;")
    LiveData<String> getListName(int id);

    @Query("Select name FROM list WHERE id = :id LIMIT 1")
    String getStaticListName(int id);

    @Query("SELECT on_created_associated_list_name FROM list WHERE id = :id LIMIT 1")
    LiveData<String> getOnCreatedAssociatedListName(int id);

    @Query("UPDATE list SET name =:newName WHERE id = :id")
    void updateName(String newName, int id);

    @Query("SELECT is_frequency_table FROM list WHERE id = :id LIMIT 1")
    LiveData<Boolean> isFrequencyTable(int id);

    @Query("SELECT associated_list FROM list WHERE id = :id")
    int getAssociatedListID(int id);
}
