package com.example.jgjio_desktop.gostats;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//TODO add Indexes

@Entity(tableName = "list")
public class StatisticalList {

    @PrimaryKey
    private int id;

    private String name;

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
