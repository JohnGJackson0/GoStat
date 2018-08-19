package com.example.jgjio_desktop.gostats;

import android.util.Log;

import java.util.List;

//Live data cannot share instances across
//EditableListActivity and ViewableListActivity
//(nor can it share instances from any Activities)
//and passing a large data list to an activity
//can cause the error TransactionTooLargeException
//so instead we can pass a singleton instance
//to save the data so that we can update the
//Live Data Model on starting ViewableActivity

public class ListHolder {
    List<DataPoint> mDataPointList;
    private static final ListHolder holder = new ListHolder();

    public List<DataPoint> getDataList() {
        return mDataPointList;
    }

    public void setDataList(List<DataPoint> list) {
       mDataPointList = list;
    }

    public static final ListHolder getInstance() {
        return holder;
    }
}
