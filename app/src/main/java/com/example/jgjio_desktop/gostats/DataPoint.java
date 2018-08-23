package com.example.jgjio_desktop.gostats;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;


//TODO test that deleting a list deletes the datapoints
@Entity(tableName = "data_point",
        foreignKeys = @ForeignKey(entity = StatisticalList.class,
                parentColumns = "id",
                childColumns = "list_id",
                onDelete = ForeignKey.CASCADE))

public class DataPoint {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "list_id")
    private int listId;

    private boolean isEnabled;

    private double value;

    public DataPoint(int listId, boolean isEnabled, double value) {
        this.listId = listId;
        this.isEnabled = isEnabled;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public double getValue() {
        return value;
    }

    public int getListId() {
        return listId;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
