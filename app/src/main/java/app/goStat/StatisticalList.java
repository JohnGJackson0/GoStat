package app.goStat;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//TODO add Indexes

@Entity(tableName = "list")
public class StatisticalList {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    @ColumnInfo(name="associated_list")
    private int associatedList = -1;

    @ColumnInfo(name = "is_frequency_table")
    private boolean isFrequencyTable;

    public StatisticalList(int id, String name, boolean isFrequencyTable) {
        this.id = id;
        this.name = name;
        this.isFrequencyTable = isFrequencyTable;
    }

    public int getId() {
        return id;
    }

    public boolean isFrequencyTable() {
        return isFrequencyTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Some lists would be associated with another list of the same class
    //  such as a frequency table
    public void setAssociatedList(int id) {
        associatedList = id;
    }

    public int getAssociatedList() {
        return associatedList;
    }

}
