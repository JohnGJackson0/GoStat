package app.goStat.model;

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

    //the associated list can be deleted, we need static version of name
    @ColumnInfo(name="on_created_associated_list_name")
    private String onCreatedAssociatedListName;

    @ColumnInfo(name = "is_frequency_table")
    private boolean isFrequencyTable;

    public StatisticalList(int id, String name, boolean isFrequencyTable, String onCreatedAssociatedListName) {
        this.id = id;
        this.name = name;
        this.isFrequencyTable = isFrequencyTable;
        this.onCreatedAssociatedListName = onCreatedAssociatedListName;
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

    public String getOnCreatedAssociatedListName() {return onCreatedAssociatedListName; };

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
