package com.example.jgjio_desktop.gostats.Repository;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

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

    //ROOM needs access to ID in some form, so pass in null or 0 for ID
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

    //this list would be associated with another list of same class
    //  such as a frequency table
    public void setAssociatedList(int id) {
        associatedList = id;
    }

    public int getAssociatedList() {
        return associatedList;
    }

    public boolean hasAssociatedList() {
        return (associatedList != -1);
    }

}
