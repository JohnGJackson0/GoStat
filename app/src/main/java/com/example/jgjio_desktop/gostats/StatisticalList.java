package com.example.jgjio_desktop.gostats;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//TODO add Indexes

@Entity(tableName = "list")
public class StatisticalList {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    //ROOM needs access to ID in some form, so pass in null or 0 for ID
    public StatisticalList(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
